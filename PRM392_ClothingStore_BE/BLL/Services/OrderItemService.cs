using AutoMapper;
using BLL.DTOs;
using BLL.Services.Interfaces;
using DAL.Models;
using DAL.Repositories.Interfaces;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace BLL.Services
{
    public class OrderItemService : IOrderItemService
    {
        private readonly IOrderItemRepository _orderItemRepository;
        private readonly IMapper _mapper;

        public OrderItemService(IOrderItemRepository orderItemRepository, IMapper mapper)
        {
            _orderItemRepository = orderItemRepository;
            _mapper = mapper;
        }

        public async Task<OrderItemDTO> GetByIdAsync(int id)
        {
            var orderItem = await _orderItemRepository.GetByIdAsync(id);
            return _mapper.Map<OrderItemDTO>(orderItem);
        }

        public async Task<IEnumerable<OrderItemDTO>> GetAllAsync()
        {
            var orderItems = await _orderItemRepository.GetAllAsync();
            return _mapper.Map<IEnumerable<OrderItemDTO>>(orderItems);
        }

        public async Task AddAsync(OrderItemDTO OrderItemDTO)
        {
            var orderItem = _mapper.Map<OrderItem>(OrderItemDTO);
            await _orderItemRepository.AddAsync(orderItem);
        }

        public async Task UpdateAsync(OrderItemDTO OrderItemDTO)
        {
            var orderItem = _mapper.Map<OrderItem>(OrderItemDTO);
            await _orderItemRepository.UpdateAsync(orderItem);
        }

        public async Task DeleteAsync(int id)
        {
            await _orderItemRepository.DeleteAsync(id);
        }
    }
}