using AutoMapper;
using BLL.DTOs;
using BLL.Services.Interfaces;
using DAL.Models;
using DAL.Repositories.Interfaces;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace BLL.Services
{
    public class UserService : IUserService
    {
        private readonly IUserRepository _userRepository;
        private readonly IMapper _mapper;
        private readonly IConfiguration _configuration;
        private readonly ILogger<UserService> _logger;

        public UserService(IUserRepository userRepository, IMapper mapper, IConfiguration configuration, ILogger<UserService> logger)
        {
            _userRepository = userRepository;
            _mapper = mapper;
            _configuration = configuration;
            _logger = logger;
        }

        public async Task AddAsync(UserDTO userDto)
        {
            _logger.LogInformation("Attempting to add user with email: {Email}", userDto.Email);

            if (string.IsNullOrEmpty(userDto.Name) || string.IsNullOrEmpty(userDto.Email) || string.IsNullOrEmpty(userDto.Password))
            {
                _logger.LogWarning("Invalid user data: Name, Email, or Password is empty");
                throw new ArgumentException("Name, Email, and Password are required.");
            }

            // Enforce role as "User" for registration
            if (!string.Equals(userDto.Role, "User", StringComparison.OrdinalIgnoreCase))
            {
                _logger.LogWarning("Invalid role for registration: {Role}. Only 'User' is allowed.", userDto.Role);
                throw new ArgumentException("Only customers (role 'User') can register.");
            }

            var existingUser = await _userRepository.GetByEmailAsync(userDto.Email);
            if (existingUser != null)
            {
                _logger.LogWarning("User with email {Email} already exists", userDto.Email);
                throw new ArgumentException("Email is already in use.");
            }

            var user = _mapper.Map<User>(userDto);
            user.Password = BCrypt.Net.BCrypt.HashPassword(userDto.Password); // Hash password
            await _userRepository.AddAsync(user);
            _logger.LogInformation("User added successfully with email: {Email}", userDto.Email);
        }

        public async Task<(string Token, UserDTO User)> LoginAsync(LoginDTO loginDto)
        {
            _logger.LogInformation("Attempting login for email: {Email}", loginDto.Email);

            if (string.IsNullOrEmpty(loginDto.Email) || string.IsNullOrEmpty(loginDto.Password))
            {
                _logger.LogWarning("Invalid login attempt: Email or password is empty");
                throw new ArgumentException("Email and password are required.");
            }

            var user = await _userRepository.GetByEmailAsync(loginDto.Email);
            if (user == null)
            {
                _logger.LogWarning("User not found for email: {Email}", loginDto.Email);
                throw new UnauthorizedAccessException("Invalid email or password.");
            }

            if (!user.Password.StartsWith("$2a$") && !user.Password.StartsWith("$2b$") && !user.Password.StartsWith("$2y$"))
            {
                _logger.LogError("Invalid password hash format for user: {Email}", loginDto.Email);
                throw new UnauthorizedAccessException("Invalid password format in database.");
            }

            if (!BCrypt.Net.BCrypt.Verify(loginDto.Password, user.Password))
            {
                _logger.LogWarning("Password verification failed for email: {Email}", loginDto.Email);
                throw new UnauthorizedAccessException("Invalid email or password.");
            }

            _logger.LogInformation("Password verified for email: {Email}", loginDto.Email);

            // Generate JWT token
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]);
            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(new[]
                {
                    new Claim(ClaimTypes.NameIdentifier, user.Id.ToString()),
                    new Claim(ClaimTypes.Email, user.Email),
                    new Claim(ClaimTypes.Role, user.Role)
                }),
                Expires = DateTime.UtcNow.AddHours(1),
                SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature),
                Issuer = _configuration["Jwt:Issuer"],
                Audience = _configuration["Jwt:Audience"]
            };

            var token = tokenHandler.CreateToken(tokenDescriptor);
            var tokenString = tokenHandler.WriteToken(token);
            _logger.LogInformation("JWT token generated for email: {Email}", loginDto.Email);

            var userDto = _mapper.Map<UserDTO>(user);
            return (tokenString, userDto);
        }

        public async Task<UserDTO> GetByIdAsync(int id)
        {
            var user = await _userRepository.GetByIdAsync(id);
            return _mapper.Map<UserDTO>(user);
        }

        public async Task<IEnumerable<UserDTO>> GetAllAsync()
        {
            var users = await _userRepository.GetAllAsync();
            return _mapper.Map<IEnumerable<UserDTO>>(users);
        }

        public async Task UpdateAsync(UserDTO userDto)
        {
            var user = _mapper.Map<User>(userDto);
            user.Password = BCrypt.Net.BCrypt.HashPassword(userDto.Password); // Hash password
            await _userRepository.UpdateAsync(user);
        }

        public async Task DeleteAsync(int id)
        {
            await _userRepository.DeleteAsync(id);
        }
    }
}