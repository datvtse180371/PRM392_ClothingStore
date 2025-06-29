using BLL.DTOs;
using BLL.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace PRM392_ClothingStore_BE.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PaymentController : ControllerBase
    {
        private readonly IPaymentService _paymentService;

        public PaymentController(IPaymentService paymentService)
        {
            _paymentService = paymentService;
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<PaymentDTO>> GetById(int id)
        {
            var payment = await _paymentService.GetByIdAsync(id);
            if (payment == null) return NotFound();
            return Ok(payment);
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<PaymentDTO>>> GetAll()
        {
            var payments = await _paymentService.GetAllAsync();
            return Ok(payments);
        }

        [HttpPost]
        public async Task<ActionResult> Add(PaymentDTO PaymentDTO)
        {
            await _paymentService.AddAsync(PaymentDTO);
            return CreatedAtAction(nameof(GetById), new { id = PaymentDTO.Id }, PaymentDTO);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult> Update(int id, PaymentDTO PaymentDTO)
        {
            if (id != PaymentDTO.Id) return BadRequest();
            await _paymentService.UpdateAsync(PaymentDTO);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(int id)
        {
            await _paymentService.DeleteAsync(id);
            return NoContent();
        }
    }
}