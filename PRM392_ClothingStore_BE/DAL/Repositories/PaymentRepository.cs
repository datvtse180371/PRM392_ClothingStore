using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DAL.Models;
using DAL.Repositories.Interfaces;

namespace DAL.Repositories
{
    public class PaymentRepository : IPaymentRepository
    {
        public Task AddPaymentAsync(Payment payment)
        {
            throw new NotImplementedException();
        }

        public Task DeletePaymentAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<Payment>> GetAllPaymentsAsync()
        {
            throw new NotImplementedException();
        }

        public Task<Payment> GetPaymentByIdAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<Payment>> GetPaymentsByUserIdAsync(int userId)
        {
            throw new NotImplementedException();
        }

        public Task UpdatePaymentAsync(Payment payment)
        {
            throw new NotImplementedException();
        }
    }
}
