using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BLL.DTOs;
using BLL.Services.Interfaces;

namespace BLL.Services
{
    public class CategoryService : ICategoryService
    {
        public Task AddCategoryAsync(CategoryDTO categoryDto)
        {
            throw new NotImplementedException();
        }

        public Task DeleteCategoryAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<CategoryDTO>> GetAllCategoriesAsync()
        {
            throw new NotImplementedException();
        }

        public Task<CategoryDTO> GetCategoryByIdAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<CategoryDTO> GetCategoryByNameAsync(string name)
        {
            throw new NotImplementedException();
        }

        public Task UpdateCategoryAsync(CategoryDTO categoryDto)
        {
            throw new NotImplementedException();
        }
    }
}
