using BLL.DTOs;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace BLL.Services.Interfaces
{
    public interface ICartItemService
    {
        Task<CartItemDTO> GetByIdAsync(int id);
        Task<IEnumerable<CartItemDTO>> GetAllAsync();
        Task<IEnumerable<CartItemDTO>> GetByUserIdAsync(int userId);
        Task<CartNotificationDTO> GetCartNotificationAsync(int userId);
        Task AddAsync(CartItemDTO cartItemDto);
        Task UpdateAsync(CartItemDTO cartItemDto);
        Task DeleteAsync(int id);
        Task AddToCartAsync(int userId, int productId, int quantity);
        Task UpdateQuantityAsync(int cartItemId, int quantity);
        Task<OrderDTO> CheckoutAsync(int userId, string paymentMethod);
    }
}