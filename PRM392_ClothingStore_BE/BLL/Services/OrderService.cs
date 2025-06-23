using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BLL.DTOs;
using BLL.Services.Interfaces;

namespace BLL.Services
{
    public class OrderService : IOrderService
    {
        public Task AddOrderAsync(OrderDTO orderDto)
        {
            throw new NotImplementedException();
        }

        public Task DeleteOrderAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<OrderDTO>> GetAllOrdersAsync()
        {
            throw new NotImplementedException();
        }

        public Task<OrderDTO> GetOrderByIdAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<OrderDTO>> GetOrdersByUserIdAsync(int userId)
        {
            throw new NotImplementedException();
        }

        public Task UpdateOrderAsync(OrderDTO orderDto)
        {
            throw new NotImplementedException();
        }
    }
}
