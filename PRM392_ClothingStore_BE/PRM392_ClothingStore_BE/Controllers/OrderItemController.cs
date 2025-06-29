using BLL.DTOs;
using BLL.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace PRM392_ClothingStore_BE.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OrderItemController : ControllerBase
    {
        private readonly IOrderItemService _orderItemService;

        public OrderItemController(IOrderItemService orderItemService)
        {
            _orderItemService = orderItemService;
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<OrderItemDTO>> GetById(int id)
        {
            var orderItem = await _orderItemService.GetByIdAsync(id);
            if (orderItem == null) return NotFound();
            return Ok(orderItem);
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<OrderItemDTO>>> GetAll()
        {
            var orderItems = await _orderItemService.GetAllAsync();
            return Ok(orderItems);
        }

        [HttpPost]
        public async Task<ActionResult> Add(OrderItemDTO OrderItemDTO)
        {
            await _orderItemService.AddAsync(OrderItemDTO);
            return CreatedAtAction(nameof(GetById), new { id = OrderItemDTO.Id }, OrderItemDTO);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult> Update(int id, OrderItemDTO OrderItemDTO)
        {
            if (id != OrderItemDTO.Id) return BadRequest();
            await _orderItemService.UpdateAsync(OrderItemDTO);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(int id)
        {
            await _orderItemService.DeleteAsync(id);
            return NoContent();
        }
    }
}