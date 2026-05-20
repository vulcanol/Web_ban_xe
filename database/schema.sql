CREATE DATABASE IF NOT EXISTS web_ban_xe
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE web_ban_xe;

-- ============================================================
-- 1. USERS – Người dùng (khách hàng, người bán, admin)
-- ============================================================
CREATE TABLE users (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  ho_ten          VARCHAR(100)  NOT NULL,
  email           VARCHAR(150)  NOT NULL UNIQUE,
  mat_khau        VARCHAR(255)  NOT NULL,
  so_dien_thoai   VARCHAR(15),
  vai_tro         ENUM('khach_hang', 'nguoi_ban', 'admin') NOT NULL DEFAULT 'khach_hang',
  dia_chi         VARCHAR(255),
  avatar_url      VARCHAR(500),
  is_active       TINYINT(1)    NOT NULL DEFAULT 1,
  email_verified  TINYINT(1)    NOT NULL DEFAULT 0,
  ngay_tao        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ngay_cap_nhat   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================================
-- 2. BRANDS – Thương hiệu xe
-- ============================================================
CREATE TABLE brands (
  id        INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  ten_hang  VARCHAR(100) NOT NULL UNIQUE,
  quoc_gia  VARCHAR(100),
  logo_url  VARCHAR(500),
  is_active TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

-- ============================================================
-- 3. CATEGORIES – Loại xe (Sedan, SUV, Pickup, ...)
-- ============================================================
CREATE TABLE categories (
  id       INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  ten_loai VARCHAR(100) NOT NULL UNIQUE,
  mo_ta    TEXT,
  icon_url VARCHAR(500)
) ENGINE=InnoDB;

-- ============================================================
-- 4. CARS – Mẫu xe (thông số kỹ thuật cố định)
-- ============================================================
CREATE TABLE cars (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  thuong_hieu_id  INT UNSIGNED NOT NULL,
  loai_xe_id      INT UNSIGNED NOT NULL,
  ten_xe          VARCHAR(200) NOT NULL,
  nam_san_xuat    YEAR         NOT NULL,
  nhien_lieu      ENUM('xang', 'dau', 'dien', 'hybrid', 'khac') NOT NULL DEFAULT 'xang',
  hop_so          ENUM('so_tu_dong', 'so_san', 'cvt', 'ban_tu_dong') NOT NULL DEFAULT 'so_tu_dong',
  so_cho          TINYINT UNSIGNED NOT NULL DEFAULT 5,
  dong_co         VARCHAR(100),
  cong_suat       FLOAT COMMENT 'Mã lực (HP)',
  dung_tich_xy_lanh FLOAT COMMENT 'Lít',
  kieu_dang       VARCHAR(100),
  FOREIGN KEY (thuong_hieu_id) REFERENCES brands(id)     ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (loai_xe_id)     REFERENCES categories(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 5. LISTINGS – Tin đăng bán xe
-- ============================================================
CREATE TABLE listings (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nguoi_dung_id   INT UNSIGNED NOT NULL,
  xe_id           INT UNSIGNED NOT NULL,
  tieu_de         VARCHAR(255) NOT NULL,
  gia_ban         DECIMAL(15,0) NOT NULL COMMENT 'VND',
  so_km           INT UNSIGNED  NOT NULL DEFAULT 0,
  tinh_trang      ENUM('moi', 'cu') NOT NULL DEFAULT 'cu',
  mau_sac         VARCHAR(50),
  bien_so         VARCHAR(20),
  mo_ta           TEXT,
  tinh_thanh      VARCHAR(100),
  quan_huyen      VARCHAR(100),
  trang_thai      ENUM('cho_duyet', 'dang_dang', 'da_ban', 'het_han', 'bi_an') NOT NULL DEFAULT 'cho_duyet',
  luot_xem        INT UNSIGNED NOT NULL DEFAULT 0,
  gio_het_han     TIMESTAMP    NULL,
  ngay_dang       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ngay_cap_nhat   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (nguoi_dung_id) REFERENCES users(id) ON DELETE CASCADE  ON UPDATE CASCADE,
  FOREIGN KEY (xe_id)         REFERENCES cars(id)  ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 6. LISTING_IMAGES – Ảnh tin đăng
-- ============================================================
CREATE TABLE listing_images (
  id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  tin_dang_id INT UNSIGNED NOT NULL,
  url_anh     VARCHAR(500) NOT NULL,
  la_anh_chinh TINYINT(1)  NOT NULL DEFAULT 0,
  thu_tu      TINYINT UNSIGNED NOT NULL DEFAULT 0,
  FOREIGN KEY (tin_dang_id) REFERENCES listings(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 7. ORDERS – Đơn hàng / Giao dịch
-- ============================================================
CREATE TABLE orders (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nguoi_mua_id    INT UNSIGNED NOT NULL,
  tin_dang_id     INT UNSIGNED NOT NULL,
  gia_giao_dich   DECIMAL(15,0) NOT NULL COMMENT 'Giá thực tế thỏa thuận',
  trang_thai      ENUM('cho_xac_nhan', 'dang_xu_ly', 'hoan_thanh', 'huy_bo') NOT NULL DEFAULT 'cho_xac_nhan',
  ghi_chu         TEXT,
  ngay_tao        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ngay_cap_nhat   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (nguoi_mua_id) REFERENCES users(id)    ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (tin_dang_id)  REFERENCES listings(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 8. PAYMENTS – Thanh toán
-- ============================================================
CREATE TABLE payments (
  id                INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  don_hang_id       INT UNSIGNED NOT NULL,
  so_tien           DECIMAL(15,0) NOT NULL COMMENT 'VND',
  phuong_thuc       ENUM('tien_mat', 'chuyen_khoan', 'vnpay', 'momo', 'zalopay', 'khac') NOT NULL,
  trang_thai        ENUM('cho_xu_ly', 'thanh_cong', 'that_bai', 'hoan_tien') NOT NULL DEFAULT 'cho_xu_ly',
  ma_giao_dich      VARCHAR(200) UNIQUE,
  thong_tin_them    JSON,
  ngay_thanh_toan   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (don_hang_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 9. REVIEWS – Đánh giá người bán
-- ============================================================
CREATE TABLE reviews (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nguoi_viet_id   INT UNSIGNED NOT NULL,
  nguoi_ban_id    INT UNSIGNED NOT NULL,
  tin_dang_id     INT UNSIGNED,
  diem_danh_gia   TINYINT UNSIGNED NOT NULL CHECK (diem_danh_gia BETWEEN 1 AND 5),
  noi_dung        TEXT,
  ngay_viet       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (nguoi_viet_id) REFERENCES users(id)    ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (nguoi_ban_id)  REFERENCES users(id)    ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (tin_dang_id)   REFERENCES listings(id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- 10. FAVORITES – Tin đăng đã lưu
-- ============================================================
CREATE TABLE favorites (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nguoi_dung_id   INT UNSIGNED NOT NULL,
  tin_dang_id     INT UNSIGNED NOT NULL,
  ngay_luu        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_fav (nguoi_dung_id, tin_dang_id),
  FOREIGN KEY (nguoi_dung_id) REFERENCES users(id)    ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (tin_dang_id)   REFERENCES listings(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- INDEXES – Tối ưu truy vấn
-- ============================================================
CREATE INDEX idx_listings_trang_thai   ON listings(trang_thai);
CREATE INDEX idx_listings_gia          ON listings(gia_ban);
CREATE INDEX idx_listings_tinh_thanh   ON listings(tinh_thanh);
CREATE INDEX idx_listings_ngay_dang    ON listings(ngay_dang DESC);
CREATE INDEX idx_cars_thuong_hieu      ON cars(thuong_hieu_id);
CREATE INDEX idx_cars_loai_xe          ON cars(loai_xe_id);
CREATE INDEX idx_orders_trang_thai     ON orders(trang_thai);
CREATE INDEX idx_reviews_nguoi_ban     ON reviews(nguoi_ban_id);

-- ============================================================
-- DỮ LIỆU MẪU
-- ============================================================

-- Thương hiệu
INSERT INTO brands (ten_hang, quoc_gia) VALUES
  ('Toyota',    'Nhật Bản'),
  ('Honda',     'Nhật Bản'),
  ('Hyundai',   'Hàn Quốc'),
  ('Kia',       'Hàn Quốc'),
  ('Ford',      'Mỹ'),
  ('Mitsubishi','Nhật Bản'),
  ('Mazda',     'Nhật Bản'),
  ('VinFast',   'Việt Nam');

-- Loại xe
INSERT INTO categories (ten_loai, mo_ta) VALUES
  ('Sedan',     'Xe con 4 cửa'),
  ('SUV',       'Xe gầm cao, đa dụng'),
  ('Pickup',    'Xe bán tải'),
  ('MPV',       'Xe đa dụng nhiều chỗ'),
  ('Hatchback', 'Xe cỡ nhỏ cửa hậu'),
  ('Điện',      'Xe chạy điện');

-- Người dùng mẫu (mật khẩu: hash của "password123")
INSERT INTO users (ho_ten, email, mat_khau, so_dien_thoai, vai_tro) VALUES
  ('Nguyễn Văn An',  'an.nguyen@email.com',  '$2b$12$hash1...', '0901234567', 'nguoi_ban'),
  ('Trần Thị Bình',  'binh.tran@email.com',  '$2b$12$hash2...', '0912345678', 'khach_hang'),
  ('Lê Văn Cường',   'cuong.le@email.com',   '$2b$12$hash3...', '0923456789', 'nguoi_ban'),
  ('Admin System',   'admin',                '$2b$12$hash4...', '1900000000', 'admin');

-- Mẫu xe
INSERT INTO cars (thuong_hieu_id, loai_xe_id, ten_xe, nam_san_xuat, nhien_lieu, hop_so, so_cho, dong_co, cong_suat) VALUES
  (1, 1, 'Toyota Camry 2.5Q', 2022, 'xang', 'so_tu_dong', 5, '2AR-FE 2.5L', 181),
  (2, 5, 'Honda City RS',     2023, 'xang', 'cvt',        5, 'L15Z9 1.5L',  121),
  (7, 1, 'Mazda3 1.5L Sport', 2023, 'xang', 'so_tu_dong', 5, 'P5-VPS 1.5L', 120),
  (8, 6, 'VinFast VF8 Plus',  2023, 'dien', 'so_tu_dong', 5, 'Motor điện', 402);

-- Tin đăng mẫu
INSERT INTO listings (nguoi_dung_id, xe_id, tieu_de, gia_ban, so_km, tinh_trang, mau_sac, mo_ta, tinh_thanh, trang_thai) VALUES
  (1, 1, 'Bán Toyota Camry 2.5Q 2022 - Xe gia đình ít đi', 1050000000, 18000, 'cu', 'Trắng ngọc trai', 'Xe cá nhân, đầy đủ đồ chơi, bảo dưỡng đúng hạn tại hãng.', 'Hà Nội',   'dang_dang'),
  (3, 3, 'Mazda3 Sport 2023 siêu lướt - Biển đẹp',         680000000,  5000,  'cu', 'Đỏ pha lê',       'Xe mua cuối năm 2023, đi ít, còn bảo hành hãng.', 'Đà Nẵng', 'dang_dang'),
  (1, 4, 'VinFast VF8 Plus full pin 2023 chính chủ',       830000000,  12000, 'cu', 'Xanh dương',      'Trang bị đầy đủ, pin tốt, sạc nhanh DC 150kW.', 'Hà Nội',   'dang_dang');

-- Ảnh tin đăng mẫu
INSERT INTO listing_images (tin_dang_id, url_anh, la_anh_chinh, thu_tu) VALUES
  (1, 'https://cdn.webbánxe.vn/1/ngoai-that-truoc.jpg',  1, 1),
  (1, 'https://cdn.webbánxe.vn/1/noi-that.jpg',          0, 2),
  (2, 'https://cdn.webbánxe.vn/2/mazda3-truoc.jpg',      1, 1),
  (3, 'https://cdn.webbánxe.vn/3/vf8-toan-canh.jpg',     1, 1);
