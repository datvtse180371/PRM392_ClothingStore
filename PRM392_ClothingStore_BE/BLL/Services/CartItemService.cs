using AutoMapper;
using BLL.DTOs;
using BLL.Services.Interfaces;
using DAL.Models;
using DAL.Repositories.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BLL.Services
{
    public class CartItemService : ICartItemService
    {
        private readonly ICartItemRepository _cartItemRepository;
        private readonly Prm392ClothingStoreDbContext _context;
        private readonly IMapper _mapper;

        public CartItemService(ICartItemRepository cartItemRepository, Prm392ClothingStoreDbContext context, IMapper mapper)
        {
            _cartItemRepository = cartItemRepository;
            _context = context;
            _mapper = mapper;
        }

        public async Task<CartItemDTO> GetByIdAsync(int id)
        {
            var cartItem = await _cartItemRepository.GetByIdAsync(id);
            return _mapper.Map<CartItemDTO>(cartItem);
        }

        public async Task<IEnumerable<CartItemDTO>> GetAllAsync()
        {
            var cartItems = await _cartItemRepository.GetAllAsync();
            return _mapper.Map<IEnumerable<CartItemDTO>>(cartItems);
        }

        public async Task AddAsync(CartItemDTO CartItemDTO)
        {
            var cartItem = _mapper.Map<CartItem>(CartItemDTO);
            await _cartItemRepository.AddAsync(cartItem);
        }

        public async Task UpdateAsync(CartItemDTO CartItemDTO)
        {
            var cartItem = _mapper.Map<CartItem>(CartItemDTO);
            await _cartItemRepository.UpdateAsync(cartItem);
        }

        public async Task DeleteAsync(int id)
        {
            await _cartItemRepository.DeleteAsync(id);
        }

        public async Task<IEnumerable<CartItemDTO>> GetByUserIdAsync(int userId)
        {
            var cartItems = await _cartItemRepository.GetByUserIdAsync(userId);
            return _mapper.Map<IEnumerable<CartItemDTO>>(cartItems);
        }

        public async Task<CartNotificationDTO> GetCartNotificationAsync(int userId)
        {
            var cartItems = await _cartItemRepository.GetByUserIdAsync(userId);
            var cartItemsWithProduct = _mapper.Map<List<CartItemWithProductDTO>>(cartItems);

            var totalItems = cartItems.Sum(c => c.Quantity);
            var uniqueProducts = cartItems.Count();
            var hasItems = totalItems > 0;

            string message = hasItems
                ? $"You have {totalItems} item(s) in your cart from {uniqueProducts} product(s)"
                : "Your cart is empty";

            return new CartNotificationDTO
            {
                HasItems = hasItems,
                TotalItems = totalItems,
                UniqueProducts = uniqueProducts,
                Message = message,
                CartItems = cartItemsWithProduct
            };
        }

        public async Task AddToCartAsync(int userId, int productId, int quantity)
        {
            // Get product to check stock
            var product = await _context.Products.FindAsync(productId);
            if (product == null)
            {
                throw new ArgumentException("Product not found");
            }

            // Check if item already exists in cart
            var existingCartItems = await _cartItemRepository.GetByUserIdAsync(userId);
            var existingItem = existingCartItems.FirstOrDefault(c => c.ProductId == productId);

            int totalQuantityNeeded = quantity;
            if (existingItem != null)
            {
                totalQuantityNeeded += existingItem.Quantity;
            }

            // Check stock availability
            if (product.Stock < totalQuantityNeeded)
            {
                throw new InvalidOperationException($"Insufficient stock for product '{product.Name}'. Available: {product.Stock}, Requested: {totalQuantityNeeded}");
            }

            if (existingItem != null)
            {
                // Update quantity if item already exists
                existingItem.Quantity += quantity;
                await _cartItemRepository.UpdateAsync(existingItem);
            }
            else
            {
                // Add new item to cart
                var cartItem = new CartItem
                {
                    UserId = userId,
                    ProductId = productId,
                    Quantity = quantity,
                    AddedAt = DateTime.Now
                };
                await _cartItemRepository.AddAsync(cartItem);
            }
        }

        public async Task UpdateQuantityAsync(int cartItemId, int quantity)
        {
            if (quantity <= 0)
            {
                throw new ArgumentException("Quantity must be greater than 0");
            }

            var cartItem = await _cartItemRepository.GetByIdAsync(cartItemId);
            if (cartItem == null)
            {
                throw new ArgumentException("Cart item not found");
            }

            cartItem.Quantity = quantity;
            await _cartItemRepository.UpdateAsync(cartItem);
        }

        public async Task<OrderDTO> CheckoutAsync(int userId, string paymentMethod)
        {
            // Get cart items for user
            var cartItems = await _cartItemRepository.GetByUserIdAsync(userId);
            if (!cartItems.Any())
            {
                throw new InvalidOperationException("Cart is empty");
            }

            // Refresh product data to ensure we have the latest stock information
            var productsToUpdate = new Dictionary<int, Product>();
            
            foreach (var cartItem in cartItems)
            {
                // Skip items with null ProductId (shouldn't happen, but just in case)
                if (!cartItem.ProductId.HasValue)
                {
                    throw new InvalidOperationException("Cart item has no associated product");
                }
                
                int productId = cartItem.ProductId.Value; // Convert nullable int to int
                
                // Reload the product to get the latest stock information
                var freshProduct = await _context.Products.FindAsync(productId);
                if (freshProduct == null)
                {
                    throw new InvalidOperationException($"Product with ID {productId} no longer exists");
                }
                
                // Check stock availability with the latest data
                if (freshProduct.Stock < cartItem.Quantity)
                {
                    throw new InvalidOperationException($"Insufficient stock for product '{freshProduct.Name}'. Available: {freshProduct.Stock}, Requested: {cartItem.Quantity}");
                }
                
                // Store the fresh product reference for later update
                productsToUpdate[productId] = freshProduct;
            }

            // Calculate total amount
            var totalAmount = cartItems.Sum(c => c.Product.Price * c.Quantity);

            // Create order entity directly to get the generated ID
            var orderEntity = new Order
            {
                UserId = userId,
                OrderDate = DateTime.Now,
                TotalAmount = totalAmount,
                Status = "Pending"
            };

            // Add order and get the generated ID
            await _context.Orders.AddAsync(orderEntity);
            await _context.SaveChangesAsync();

            // Create order items from cart items and reduce stock
            foreach (var cartItem in cartItems)
            {
                var orderItemEntity = new OrderItem
                {
                    OrderId = orderEntity.Id,
                    ProductId = cartItem.ProductId.Value, // Use .Value to convert nullable int to int
                    Quantity = cartItem.Quantity,
                    UnitPrice = cartItem.Product.Price
                };
                await _context.OrderItems.AddAsync(orderItemEntity);

                // Reduce product stock using the fresh product reference
                int productId = cartItem.ProductId.Value; // Convert nullable int to int
                if (productsToUpdate.TryGetValue(productId, out var product))
                {
                    Console.WriteLine($"Reducing stock for product {product.Name} (ID: {product.Id}): Current stock: {product.Stock}, Reducing by: {cartItem.Quantity}, New stock will be: {product.Stock - cartItem.Quantity}");
                    product.Stock -= cartItem.Quantity;
                    _context.Products.Update(product);
                }
            }

            // Create payment record
            var paymentEntity = new Payment
            {
                OrderId = orderEntity.Id,
                PaymentMethod = paymentMethod,
                PaymentDate = DateTime.Now,
                Status = "Completed"
            };
            await _context.Payments.AddAsync(paymentEntity);

            // Save order items and payment
            await _context.SaveChangesAsync();

            // Clear cart after successful checkout
            foreach (var cartItem in cartItems)
            {
                await _cartItemRepository.DeleteAsync(cartItem.Id);
            }

            // Return the order as DTO
            return _mapper.Map<OrderDTO>(orderEntity);
        }
    }
}
