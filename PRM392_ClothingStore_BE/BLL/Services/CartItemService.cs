using AutoMapper;
using BLL.DTOs;
using BLL.Services.Interfaces;
using DAL.Models;
using DAL.Repositories.Interfaces;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace BLL.Services
{
    public class CartItemService : ICartItemService
    {
        private readonly ICartItemRepository _cartItemRepository;
        private readonly IMapper _mapper;

        public CartItemService(ICartItemRepository cartItemRepository, IMapper mapper)
        {
            _cartItemRepository = cartItemRepository;
            _mapper = mapper;
        }

        public async Task<CartItemDTO> GetByIdAsync(int id)
        {
            var cartItem = await _cartItemRepository.GetByIdAsync(id);
            return _mapper.Map<CartItemDTO>(cartItem);
        }

        public async Task<IEnumerable<CartItemDTO>> GetAllAsync()
        {
            var cartItems = await _cartItemRepository.GetAllAsync();
            return _mapper.Map<IEnumerable<CartItemDTO>>(cartItems);
        }

        public async Task AddAsync(CartItemDTO CartItemDTO)
        {
            var cartItem = _mapper.Map<CartItem>(CartItemDTO);
            await _cartItemRepository.AddAsync(cartItem);
        }

        public async Task UpdateAsync(CartItemDTO CartItemDTO)
        {
            var cartItem = _mapper.Map<CartItem>(CartItemDTO);
            await _cartItemRepository.UpdateAsync(cartItem);
        }

        public async Task DeleteAsync(int id)
        {
            await _cartItemRepository.DeleteAsync(id);
        }
    }
}