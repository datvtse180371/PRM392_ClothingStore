using DAL.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace DAL.Repositories.Interfaces
{
    public interface ICartItemRepository
    {
        Task<CartItem> GetByIdAsync(int id);
        Task<IEnumerable<CartItem>> GetAllAsync();
        Task<IEnumerable<CartItem>> GetByUserIdAsync(int userId);
        Task<int> GetCartItemCountByUserIdAsync(int userId);
        Task AddAsync(CartItem cartItem);
        Task UpdateAsync(CartItem cartItem);
        Task DeleteAsync(int id);
    }
}