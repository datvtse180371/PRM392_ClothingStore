using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BLL.DTOs;

namespace BLL.Services.Interfaces
{
    public interface IOrderItemService
    {
        Task<OrderItemDTO> GetOrderItemByIdAsync(int id);
        Task<IEnumerable<OrderItemDTO>> GetAllOrderItemsAsync();
        Task<IEnumerable<OrderItemDTO>> GetOrderItemsByOrderIdAsync(int orderId);
        Task AddOrderItemAsync(OrderItemDTO orderItemDto);
        Task UpdateOrderItemAsync(OrderItemDTO orderItemDto);
        Task DeleteOrderItemAsync(int id);
    }
}
