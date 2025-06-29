using BLL.DTOs;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace BLL.Services.Interfaces
{
    public interface ICartItemService
    {
        Task<CartItemDTO> GetByIdAsync(int id);
        Task<IEnumerable<CartItemDTO>> GetAllAsync();
        Task AddAsync(CartItemDTO cartItemDto);
        Task UpdateAsync(CartItemDTO cartItemDto);
        Task DeleteAsync(int id);
    }
}