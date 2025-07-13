using BLL.DTOs;
using BLL.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Security.Claims;
using System.Threading.Tasks;

namespace PRM392_ClothingStore_BE.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CartItemController : ControllerBase
    {
        private readonly ICartItemService _cartItemService;

        public CartItemController(ICartItemService cartItemService)
        {
            _cartItemService = cartItemService;
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<CartItemDTO>> GetById(int id)
        {
            var cartItem = await _cartItemService.GetByIdAsync(id);
            if (cartItem == null) return NotFound();
            return Ok(cartItem);
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<CartItemDTO>>> GetAll()
        {
            var cartItems = await _cartItemService.GetAllAsync();
            return Ok(cartItems);
        }

        [HttpPost]
        public async Task<ActionResult> Add(CartItemDTO CartItemDTO)
        {
            await _cartItemService.AddAsync(CartItemDTO);
            return CreatedAtAction(nameof(GetById), new { id = CartItemDTO.Id }, CartItemDTO);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult> Update(int id, CartItemDTO CartItemDTO)
        {
            if (id != CartItemDTO.Id) return BadRequest();
            await _cartItemService.UpdateAsync(CartItemDTO);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(int id)
        {
            await _cartItemService.DeleteAsync(id);
            return NoContent();
        }

        [HttpGet("user/{userId}")]
        public async Task<ActionResult<IEnumerable<CartItemDTO>>> GetByUserId(int userId)
        {
            var cartItems = await _cartItemService.GetByUserIdAsync(userId);
            return Ok(cartItems);
        }

        [HttpGet("notification/{userId}")]
        public async Task<ActionResult<CartNotificationDTO>> GetCartNotification(int userId)
        {
            var notification = await _cartItemService.GetCartNotificationAsync(userId);
            return Ok(notification);
        }

        [HttpGet("notification")]
        [Authorize]
        public async Task<ActionResult<CartNotificationDTO>> GetMyCartNotification()
        {
            var userIdClaim = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            if (string.IsNullOrEmpty(userIdClaim) || !int.TryParse(userIdClaim, out int userId))
            {
                return Unauthorized("Invalid user token");
            }

            var notification = await _cartItemService.GetCartNotificationAsync(userId);
            return Ok(notification);
        }

        [HttpPost("add-to-cart")]
        public async Task<ActionResult> AddToCart([FromBody] AddToCartRequest request)
        {
            try
            {
                await _cartItemService.AddToCartAsync(request.UserId, request.ProductId, request.Quantity);
                return Ok(new { message = "Product added to cart successfully" });
            }
            catch (Exception ex)
            {
                return BadRequest(new { message = ex.Message });
            }
        }

        [HttpPut("update-quantity/{cartItemId}")]
        public async Task<ActionResult> UpdateQuantity(int cartItemId, [FromBody] UpdateQuantityRequest request)
        {
            try
            {
                await _cartItemService.UpdateQuantityAsync(cartItemId, request.Quantity);
                return Ok(new { message = "Cart item quantity updated successfully" });
            }
            catch (Exception ex)
            {
                return BadRequest(new { message = ex.Message });
            }
        }

        [HttpDelete("remove-from-cart/{cartItemId}")]
        public async Task<ActionResult> RemoveFromCart(int cartItemId)
        {
            try
            {
                await _cartItemService.DeleteAsync(cartItemId);
                return Ok(new { message = "Product removed from cart successfully" });
            }
            catch (Exception ex)
            {
                return BadRequest(new { message = ex.Message });
            }
        }

        [HttpPost("checkout/{userId}")]
        public async Task<ActionResult<OrderDTO>> Checkout(int userId, [FromBody] CheckoutRequest request)
        {
            try
            {
                var order = await _cartItemService.CheckoutAsync(userId, request.PaymentMethod);
                return Ok(order);
            }
            catch (Exception ex)
            {
                return BadRequest(new { message = ex.Message });
            }
        }
    }

    public class AddToCartRequest
    {
        public int UserId { get; set; }
        public int ProductId { get; set; }
        public int Quantity { get; set; } = 1;
    }

    public class UpdateQuantityRequest
    {
        public int Quantity { get; set; }
    }

    public class CheckoutRequest
    {
        public string PaymentMethod { get; set; } = "Credit Card";
    }
}