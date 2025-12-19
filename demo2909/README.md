# Dự án Demo Spring Boot JWT Authentication

## Tổng quan dự án
Dự án này là một bài tập minh họa việc xây dựng REST API sử dụng Spring Boot với cơ chế xác thực JWT (JSON Web Token) và phân quyền theo vai trò (Role-based Authorization: ADMIN/USER).

## Tính năng
- Đăng ký và Đăng nhập sử dụng JWT.
- Phân quyền người dùng (ADMIN/USER).
- Các thao tác CRUD cho User và Blog.
- Bảo mật các endpoint bằng Spring Security.
- Mã hóa mật khẩu sử dụng BCrypt.
- **Quyền hạn đặc biệt**:
  - ADMIN: Có toàn quyền, đặc biệt là quyền xóa User.
  - USER: Chỉ có quyền xem và cập nhật (sửa/xóa) Blog của chính mình.
- **Giao diện Frontend đơn giản**:
  - Trang đăng nhập/đăng ký.
  - Dashboard quản lý Blog.

## Công nghệ sử dụng
- Spring Boot 3.5.6
- Spring Security
- JSON Web Token (JWT)
- Spring Data JPA
- H2 Database (Database nội bộ trong bộ nhớ cho mục đích demo)
- Maven

## Hướng dẫn cài đặt và chạy

### Yêu cầu
- JDK 17 (hoặc mới hơn)
- Maven 3.6+
- Postman (để test API) hoặc cURL

### Chạy ứng dụng
1. Mở terminal tại thư mục gốc của dự án.
2. Chạy lệnh sau:
```bash
mvn spring-boot:run
```
3. Ứng dụng sẽ khởi chạy tại:
   - Frontend: `http://localhost:8080` (Tự động chuyển hướng đến trang đăng nhập hoặc dashboard)
   - API Base URL: `http://localhost:8080/api`

## Tài liệu API

### Endpoint Xác thực (Authentication)

#### 1. Đăng ký (Register)
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "admin",
    "password": "admin123",
    "role": "ADMIN"
}
```

#### 2. Đăng nhập (Login)
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "admin",
    "password": "admin123"
}
```
*Response trả về `token`. Sử dụng token này cho các request tiếp theo.*

### Endpoint cho User

#### 1. Lấy danh sách User
```http
GET /api/users
Authorization: Bearer <your_jwt_token>
```

#### 2. Xóa User (Chỉ ADMIN)
```http
DELETE /api/users/{id}
Authorization: Bearer <your_jwt_token>
```

### Endpoint cho Blog

#### 1. Tạo Blog mới
```http
POST /api/blogs
Authorization: Bearer <your_jwt_token>
Content-Type: application/json

{
    "title": "Tiêu đề Blog",
    "content": "Nội dung Blog"
}
```

#### 2. Lấy danh sách Blog
```http
GET /api/blogs
Authorization: Bearer <your_jwt_token>
```
*Lưu ý: ADMIN sẽ thấy toàn bộ blog. USER chỉ thấy blog của chính mình.*

#### 3. Cập nhật Blog (Chỉ chủ sở hữu hoặc ADMIN)
```http
PUT /api/blogs/{id}
Authorization: Bearer <your_jwt_token>
Content-Type: application/json

{
    "title": "Tiêu đề mới",
    "content": "Nội dung mới"
}
```

#### 4. Xóa Blog (Chỉ chủ sở hữu hoặc ADMIN)
```http
DELETE /api/blogs/{id}
Authorization: Bearer <your_jwt_token>
```

## Cấu hình Database
Ứng dụng sử dụng H2 Database lưu trong bộ nhớ.
- Console URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`