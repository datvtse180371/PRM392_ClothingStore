using BLL.DTOs;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace BLL.Services.Interfaces
{
    public interface IOrderService
    {
        Task<OrderDTO> GetByIdAsync(int id);
        Task<IEnumerable<OrderDTO>> GetAllAsync();
        Task<IEnumerable<OrderDTO>> GetByUserIdAsync(int userId);
        Task AddAsync(OrderDTO orderDto);
        Task UpdateAsync(OrderDTO orderDto);
        Task DeleteAsync(int id);
    }
}