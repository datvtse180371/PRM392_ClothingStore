using Microsoft.AspNetCore.Mvc;
using BLL.DTOs;
using BLL.Services.Interfaces;
using System.Threading.Tasks;
using System.Collections.Generic;

namespace PRM392_ClothingStore_BE.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CategoryController : ControllerBase
    {
        private readonly ICategoryService _categoryService;

        public CategoryController(ICategoryService categoryService) => _categoryService = categoryService;

        [HttpGet("{id}")]
        public async Task<ActionResult<CategoryDTO>> GetById(int id)
        {
            var category = await _categoryService.GetCategoryByIdAsync(id);
            return category == null ? NotFound() : Ok(category);
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<CategoryDTO>>> GetAll() =>
            Ok(await _categoryService.GetAllCategoriesAsync());

        [HttpGet("name/{name}")]
        public async Task<ActionResult<CategoryDTO>> GetByName(string name)
        {
            var category = await _categoryService.GetCategoryByNameAsync(name);
            return category == null ? NotFound() : Ok(category);
        }

        [HttpPost]
        public async Task<IActionResult> Add([FromBody] CategoryDTO dto)
        {
            await _categoryService.AddCategoryAsync(dto);
            return Ok();
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(int id, [FromBody] CategoryDTO dto)
        {
            if (id != dto.Id) return BadRequest();
            await _categoryService.UpdateCategoryAsync(dto);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            await _categoryService.DeleteCategoryAsync(id);
            return NoContent();
        }
    }
}
