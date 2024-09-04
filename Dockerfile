# Sử dụng image Postgres mới nhất
FROM postgres:latest

# Thiết lập biến môi trường cho Postgres
ENV POSTGRES_DB=jdbc:postgresql://localhost:5432/lms
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=tiendat1409

# Sao chép các file SQL để khởi tạo cơ sở dữ liệu
COPY ./initdb /docker-entrypoint-initdb.d/

# Thiết lập ENTRYPOINT để chạy Postgres
ENTRYPOINT ["docker-entrypoint.sh"]

# Chạy lệnh Postgres
CMD ["postgres"]
