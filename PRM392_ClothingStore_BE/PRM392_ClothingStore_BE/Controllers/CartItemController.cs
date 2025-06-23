using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;
using BLL.DTOs;
using BLL.Services.Interfaces;

namespace PRM392_ClothingStore_BE.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CartItemController : ControllerBase
    {
        private readonly ICartItemService _cartItemService;

        public CartItemController(ICartItemService cartItemService)
        {
            _cartItemService = cartItemService;
        }

        // GET: api/CartItem/{id}
        [HttpGet("{id}")]
        public async Task<ActionResult<CartItemDTO>> GetCartItemById(int id)
        {
            var cartItem = await _cartItemService.GetCartItemByIdAsync(id);
            if (cartItem == null)
                return NotFound();

            return Ok(cartItem);
        }

        // GET: api/CartItem/user/{userId}
        [HttpGet("user/{userId}")]
        public async Task<ActionResult<IEnumerable<CartItemDTO>>> GetCartItemsByUserId(int userId)
        {
            var items = await _cartItemService.GetCartItemsByUserIdAsync(userId);
            return Ok(items);
        }

        // POST: api/CartItem
        [HttpPost]
        public async Task<ActionResult> AddCartItem([FromBody] CartItemDTO cartItemDto)
        {
            await _cartItemService.AddCartItemAsync(cartItemDto);
            return CreatedAtAction(nameof(GetCartItemById), new { id = cartItemDto.Id }, cartItemDto);
        }

        // PUT: api/CartItem/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateCartItem(int id, [FromBody] CartItemDTO cartItemDto)
        {
            if (id != cartItemDto.Id)
                return BadRequest("ID mismatch");

            await _cartItemService.UpdateCartItemAsync(cartItemDto);
            return NoContent();
        }

        // DELETE: api/CartItem/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteCartItem(int id)
        {
            await _cartItemService.DeleteCartItemAsync(id);
            return NoContent();
        }

        // DELETE: api/CartItem/user/{userId}/clear
        [HttpDelete("user/{userId}/clear")]
        public async Task<IActionResult> ClearCartByUserId(int userId)
        {
            await _cartItemService.ClearCartByUserIdAsync(userId);
            return NoContent();
        }

        // GET: api/CartItem/user/{userId}/product/{productId}
        [HttpGet("user/{userId}/product/{productId}")]
        public async Task<ActionResult<CartItemDTO>> GetCartItemByUserAndProduct(int userId, int productId)
        {
            var cartItem = await _cartItemService.GetCartItemByUserAndProductAsync(userId, productId);
            if (cartItem == null)
                return NotFound();

            return Ok(cartItem);
        }
    }
}
