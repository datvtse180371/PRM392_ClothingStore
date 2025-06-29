using BLL.DTOs;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace BLL.Services.Interfaces
{
    public interface IOrderItemService
    {
        Task<OrderItemDTO> GetByIdAsync(int id);
        Task<IEnumerable<OrderItemDTO>> GetAllAsync();
        Task AddAsync(OrderItemDTO orderItemDto);
        Task UpdateAsync(OrderItemDTO orderItemDto);
        Task DeleteAsync(int id);
    }
}