using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BLL.DTOs;
using BLL.Services.Interfaces;

namespace BLL.Services
{
    public class OrderItemService : IOrderItemService
    {
        public Task AddOrderItemAsync(OrderItemDTO orderItemDto)
        {
            throw new NotImplementedException();
        }

        public Task DeleteOrderItemAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<OrderItemDTO>> GetAllOrderItemsAsync()
        {
            throw new NotImplementedException();
        }

        public Task<OrderItemDTO> GetOrderItemByIdAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<OrderItemDTO>> GetOrderItemsByOrderIdAsync(int orderId)
        {
            throw new NotImplementedException();
        }

        public Task UpdateOrderItemAsync(OrderItemDTO orderItemDto)
        {
            throw new NotImplementedException();
        }
    }
}
