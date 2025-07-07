using AutoMapper;
using BLL.DTOs;
using BLL.Services.Interfaces;
using DAL.Models;
using DAL.Repositories.Interfaces;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace BLL.Services
{
    public class ProductService : IProductService
    {
        private readonly IProductRepository _productRepository;
        private readonly IMapper _mapper;

        public ProductService(IProductRepository productRepository, IMapper mapper)
        {
            _productRepository = productRepository;
            _mapper = mapper;
        }

        public async Task<ProductDTO> GetByIdAsync(int id)
        {
            var product = await _productRepository.GetByIdAsync(id);
            return _mapper.Map<ProductDTO>(product);
        }

        public async Task<IEnumerable<ProductDTO>> GetAllAsync()
        {
            var products = await _productRepository.GetAllAsync();
            return _mapper.Map<IEnumerable<ProductDTO>>(products);
        }

        public async Task AddAsync(ProductDTO ProductDTO)
        {
            var product = _mapper.Map<Product>(ProductDTO);
            await _productRepository.AddAsync(product);
        }

        public async Task UpdateAsync(ProductDTO ProductDTO)
        {
            var product = _mapper.Map<Product>(ProductDTO);
            await _productRepository.UpdateAsync(product);
        }

        public async Task DeleteAsync(int id)
        {
            await _productRepository.DeleteAsync(id);
        }
        public async Task<IEnumerable<ProductDTO>> GetAllAsync(string? search = null, int? categoryId = null)
        {
            var products = await _productRepository.GetAllAsync();

            // Search by product name (case-insensitive)
            if (!string.IsNullOrEmpty(search))
            {
                products = products.Where(p => p.Name.Contains(search, StringComparison.OrdinalIgnoreCase));
            }

            // Filter by CategoryId if provided
            if (categoryId.HasValue)
            {
                products = products.Where(p => p.Category == categoryId.Value);
            }

            // Sort by CategoryId
            products = products.OrderBy(p => p.Category);

            return _mapper.Map<IEnumerable<ProductDTO>>(products);
        }

    }
}