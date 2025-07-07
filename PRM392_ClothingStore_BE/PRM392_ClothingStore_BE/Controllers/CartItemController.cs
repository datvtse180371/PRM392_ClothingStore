using BLL.DTOs;
using BLL.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Security.Claims;
using System.Threading.Tasks;

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

        [HttpGet("{id}")]
        public async Task<ActionResult<CartItemDTO>> GetById(int id)
        {
            var cartItem = await _cartItemService.GetByIdAsync(id);
            if (cartItem == null) return NotFound();
            return Ok(cartItem);
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<CartItemDTO>>> GetAll()
        {
            var cartItems = await _cartItemService.GetAllAsync();
            return Ok(cartItems);
        }

        [HttpPost]
        public async Task<ActionResult> Add(CartItemDTO CartItemDTO)
        {
            await _cartItemService.AddAsync(CartItemDTO);
            return CreatedAtAction(nameof(GetById), new { id = CartItemDTO.Id }, CartItemDTO);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult> Update(int id, CartItemDTO CartItemDTO)
        {
            if (id != CartItemDTO.Id) return BadRequest();
            await _cartItemService.UpdateAsync(CartItemDTO);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(int id)
        {
            await _cartItemService.DeleteAsync(id);
            return NoContent();
        }

        [HttpGet("user/{userId}")]
        public async Task<ActionResult<IEnumerable<CartItemDTO>>> GetByUserId(int userId)
        {
            var cartItems = await _cartItemService.GetByUserIdAsync(userId);
            return Ok(cartItems);
        }

        [HttpGet("notification/{userId}")]
        public async Task<ActionResult<CartNotificationDTO>> GetCartNotification(int userId)
        {
            var notification = await _cartItemService.GetCartNotificationAsync(userId);
            return Ok(notification);
        }

        [HttpGet("notification")]
        [Authorize]
        public async Task<ActionResult<CartNotificationDTO>> GetMyCartNotification()
        {
            var userIdClaim = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            if (string.IsNullOrEmpty(userIdClaim) || !int.TryParse(userIdClaim, out int userId))
            {
                return Unauthorized("Invalid user token");
            }

            var notification = await _cartItemService.GetCartNotificationAsync(userId);
            return Ok(notification);
        }
    }
}