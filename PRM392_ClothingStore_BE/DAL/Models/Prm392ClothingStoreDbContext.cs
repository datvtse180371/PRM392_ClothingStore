using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;

namespace DAL.Models
{
    public partial class Prm392ClothingStoreDbContext : DbContext
    {
        public Prm392ClothingStoreDbContext()
        {
        }

        public Prm392ClothingStoreDbContext(DbContextOptions<Prm392ClothingStoreDbContext> options)
            : base(options)
        {
        }

        public virtual DbSet<CartItem> CartItems { get; set; }
        public virtual DbSet<Category> Categories { get; set; }
        public virtual DbSet<Order> Orders { get; set; }
        public virtual DbSet<OrderItem> OrderItems { get; set; }
        public virtual DbSet<Payment> Payments { get; set; }
        public virtual DbSet<Product> Products { get; set; }
        public virtual DbSet<User> Users { get; set; }


        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
=> optionsBuilder.UseSqlServer("Server=(local);Database=PRM392_ClothingStore_DB;Trusted_Connection=True;Encrypt=True;TrustServerCertificate=True;");

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<CartItem>(entity =>
            {
                entity.HasKey(e => e.Id).HasName("PK__CartItem__3214EC078796B49D");

                entity.ToTable("CartItem");

                entity.Property(e => e.AddedAt)
                    .HasDefaultValueSql("GETDATE()")
                    .HasColumnType("datetime");

                entity.HasOne(d => d.Product).WithMany(p => p.CartItems)
                    .HasForeignKey(d => d.ProductId)
                    .HasConstraintName("FK__CartItem__Produc__412EB0B6");

                entity.HasOne(d => d.User).WithMany(p => p.CartItems)
                    .HasForeignKey(d => d.UserId)
                    .HasConstraintName("FK__CartItem__UserId__3F466844");

                entity.Property(e => e.Quantity).HasDefaultValue(1);
            });

            modelBuilder.Entity<Category>(entity =>
            {
                entity.HasKey(e => e.Id).HasName("PK__Category__3214EC073B5E6F36");

                entity.ToTable("Category");

                entity.Property(e => e.Description).HasMaxLength(500);
                entity.Property(e => e.Name).HasMaxLength(100).IsRequired();
            });

            modelBuilder.Entity<Order>(entity =>
            {
                entity.HasKey(e => e.Id).HasName("PK__Order__3214EC07B1761C23");

                entity.ToTable("Order");

                entity.Property(e => e.OrderDate)
                    .HasDefaultValueSql("GETDATE()")
                    .HasColumnType("datetime");

                entity.Property(e => e.Status).HasMaxLength(50).IsRequired();
                entity.Property(e => e.TotalAmount).HasColumnType("decimal(10, 2)").HasDefaultValue(0.00m);

                entity.HasOne(d => d.User).WithMany(p => p.Orders)
                    .HasForeignKey(d => d.UserId)
                    .HasConstraintName("FK__Order__UserId__44FF419A")
                    .OnDelete(DeleteBehavior.Restrict);
            });

            modelBuilder.Entity<OrderItem>(entity =>
            {
                entity.HasKey(e => e.Id).HasName("PK__OrderIte__3214EC073960B6E3");

                entity.ToTable("OrderItem");

                entity.Property(e => e.SubTotal)
                    .HasComputedColumnSql("[Quantity] * [UnitPrice]", true)
                    .HasColumnType("decimal(21, 2)");

                entity.Property(e => e.UnitPrice).HasColumnType("decimal(10, 2)").IsRequired();
                entity.Property(e => e.Quantity).IsRequired();

                entity.HasOne(d => d.Order).WithMany(p => p.OrderItems)
                    .HasForeignKey(d => d.OrderId)
                    .HasConstraintName("FK__OrderItem__Order__49C3F6B7")
                    .OnDelete(DeleteBehavior.Cascade);

                entity.HasOne(d => d.Product).WithMany(p => p.OrderItems)
                    .HasForeignKey(d => d.ProductId)
                    .HasConstraintName("FK__OrderItem__Produ__4AB81AF0")
                    .OnDelete(DeleteBehavior.Restrict);
            });

            modelBuilder.Entity<Payment>(entity =>
            {
                entity.HasKey(e => e.Id).HasName("PK__Payment__3214EC0787FA227F");

                entity.ToTable("Payment");

                entity.Property(e => e.PaymentDate)
                    .HasDefaultValueSql("GETDATE()")
                    .HasColumnType("datetime");

                entity.Property(e => e.PaymentMethod).HasMaxLength(100).IsRequired();
                entity.Property(e => e.Status).HasMaxLength(50).IsRequired();

                entity.HasOne(d => d.Order).WithMany(p => p.Payments)
                    .HasForeignKey(d => d.OrderId)
                    .HasConstraintName("FK__Payment__OrderId__4E88ABD4")
                    .OnDelete(DeleteBehavior.Cascade);
            });

            modelBuilder.Entity<Product>(entity =>
            {
                entity.HasKey(e => e.Id).HasName("PK__Product__3214EC07FCEBDF53");

                entity.ToTable("Product");

                entity.Property(e => e.Color).HasMaxLength(50);
                entity.Property(e => e.Description).HasMaxLength(500);
                entity.Property(e => e.ImageUrl).HasMaxLength(255).HasColumnName("ImageURL");
                entity.Property(e => e.Name).HasMaxLength(100).IsRequired();
                entity.Property(e => e.Price).HasColumnType("decimal(10, 2)").IsRequired();
                entity.Property(e => e.Size).HasMaxLength(50);

                entity.HasOne(d => d.CategoryNavigation).WithMany(p => p.Products)
                    .HasForeignKey(d => d.Category)
                    .HasConstraintName("FK__Product__Categor__3C69FB99")
                    .OnDelete(DeleteBehavior.Restrict);
            });

            modelBuilder.Entity<User>(entity =>
            {
                entity.HasKey(e => e.Id).HasName("PK__User__3214EC0757BB8811");

                entity.ToTable("User");

                entity.HasIndex(e => e.Email, "UQ__User__A9D105343700BA6C").IsUnique();

                entity.Property(e => e.Email).HasMaxLength(255).IsRequired();
                entity.Property(e => e.Name).HasMaxLength(100).IsRequired();
                entity.Property(e => e.Password).HasMaxLength(255).IsRequired();
                entity.Property(e => e.Role).HasMaxLength(50).IsRequired();
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}