using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BLL.DTOs;
using BLL.Services.Interfaces;

namespace BLL.Services
{
    public class CartItemService : ICartItemService
    {
        public Task AddCartItemAsync(CartItemDTO cartItemDto)
        {
            throw new NotImplementedException();
        }

        public Task ClearCartByUserIdAsync(int userId)
        {
            throw new NotImplementedException();
        }

        public Task DeleteCartItemAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<CartItemDTO> GetCartItemByIdAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<CartItemDTO> GetCartItemByUserAndProductAsync(int userId, int productId)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<CartItemDTO>> GetCartItemsByUserIdAsync(int userId)
        {
            throw new NotImplementedException();
        }

        public Task UpdateCartItemAsync(CartItemDTO cartItemDto)
        {
            throw new NotImplementedException();
        }
    }
}
