using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BLL.DTOs;

namespace BLL.Services.Interfaces
{
    public interface ICartItemService
    {
        Task<CartItemDTO> GetCartItemByIdAsync(int id);
        Task<IEnumerable<CartItemDTO>> GetCartItemsByUserIdAsync(int userId);
        Task AddCartItemAsync(CartItemDTO cartItemDto);
        Task UpdateCartItemAsync(CartItemDTO cartItemDto);
        Task DeleteCartItemAsync(int id);
        Task ClearCartByUserIdAsync(int userId);
        Task<CartItemDTO> GetCartItemByUserAndProductAsync(int userId, int productId);
    }
}
    