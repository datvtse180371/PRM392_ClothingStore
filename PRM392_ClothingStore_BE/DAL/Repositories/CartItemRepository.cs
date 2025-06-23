using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DAL.Models;
using DAL.Repositories.Interfaces;

namespace DAL.Repositories
{
    public class CartItemRepository : ICartItemRepository
    {
        public Task AddCartItemAsync(CartItem cartItem)
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

        public Task<CartItem> GetCartItemByIdAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<CartItem> GetCartItemByUserAndProductAsync(int userId, int productId)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<CartItem>> GetCartItemsByUserIdAsync(int userId)
        {
            throw new NotImplementedException();
        }

        public Task UpdateCartItemAsync(CartItem cartItem)
        {
            throw new NotImplementedException();
        }
    }
}
