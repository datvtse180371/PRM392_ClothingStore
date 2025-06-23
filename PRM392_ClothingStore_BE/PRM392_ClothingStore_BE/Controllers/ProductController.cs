using BLL.DTOs;
using BLL.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

[Route("api/[controller]")]
[ApiController]
public class ProductController : ControllerBase
{
    private readonly IProductService _service;

    public ProductController(IProductService service) => _service = service;

    [HttpGet("{id}")]
    public async Task<ActionResult<ProductDTO>> GetById(int id) =>
        Ok(await _service.GetProductByIdAsync(id));

    [HttpGet]
    public async Task<ActionResult<IEnumerable<ProductDTO>>> GetAll() =>
        Ok(await _service.GetAllProductsAsync());

    [HttpPost]
    public async Task<IActionResult> Add([FromBody] ProductDTO dto)
    {
        await _service.AddProductAsync(dto);
        return Ok();
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, [FromBody] ProductDTO dto)
    {
        if (id != dto.Id) return BadRequest();
        await _service.UpdateProductAsync(dto);
        return NoContent();
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        await _service.DeleteProductAsync(id);
        return NoContent();
    }
}
