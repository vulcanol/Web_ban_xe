package org.acme.resource;

import java.util.List;

import org.acme.auth.SessionManager;
import org.acme.auth.SessionManager.SessionData;
import org.acme.domain.Brand;
import org.acme.domain.Category;
import org.acme.domain.Listing;
import org.acme.service.BrandService;
import org.acme.service.CategoryService;
import org.acme.service.ListingService;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Trang chủ công khai — hiển thị cho tất cả khách truy cập,
 * kể cả chưa đăng nhập. Có nút Đăng Nhập / Đăng Ký trên navbar.
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class HomeController {

    static final String SESSION_COOKIE = "SESSION_ID";

    @Inject SessionManager sessionManager;
    @Inject CategoryService categoryService;
    @Inject BrandService brandService;
    @Inject ListingService listingService;

    @CheckedTemplate(basePath = "public")
    public static class Templates {
        public static native TemplateInstance home(
                SessionData session,
                List<Category> categories,
                long totalListings,
                long totalCategories,
                long totalBrands
        );
        public static native TemplateInstance carDetail(SessionData session, String carTitle, String carImage, String carDesc, String carPrice1, String carPrice2);
        public static native TemplateInstance cars(
                SessionData session,
                List<Category> categories,
                List<Brand> brands,
                List<Listing> listings
        );
    }

    @GET
    public TemplateInstance home(@CookieParam(SESSION_COOKIE) String sessionId) {
        // Lấy session nếu đã đăng nhập (để hiển thị tên user trên navbar)
        SessionData session = sessionManager.getSession(sessionId);

        List<Category> categories = categoryService.getAllCategoriesEntities();
        long totalListings = listingService.countActiveListings();
        long totalCategories = categoryService.countCategories();
        long totalBrands = brandService.countBrands();

        return Templates.home(session, categories, totalListings, totalCategories, totalBrands);
    }

    @GET
    @Path("car-detail")
    public TemplateInstance carDetail(@CookieParam(SESSION_COOKIE) String sessionId, @jakarta.ws.rs.QueryParam("type") String type) {
        SessionData session = sessionManager.getSession(sessionId);
        
        String title = "Mercedes-Maybach S-Class";
        String image = "https://images.unsplash.com/photo-1627454820516-dc7671ceafef?q=80&w=1600&auto=format&fit=crop";
        String description = "Trải nghiệm kiệt tác di chuyển với thiết kế vượt thời gian, không gian nội thất sang trọng bậc nhất và công nghệ tiên tiến.";
        String price1 = "8.199.000.000 đ";
        String price2 = "14.600.000.000 đ";
        
        if ("sedan".equals(type)) {
            title = "Sedan Sang Trọng";
            image = "https://images.unsplash.com/photo-1605559424843-9e4c228bf1c2?q=80&w=1600&auto=format&fit=crop";
            description = "Tuyệt tác thiết kế vượt thời gian, mang lại trải nghiệm đỉnh cao với khoang nội thất vô cùng rộng rãi và êm ái.";
            price1 = "4.299.000.000 đ";
            price2 = "6.550.000.000 đ";
        } else if ("suv".equals(type)) {
            title = "SUV Đa Dụng";
            image = "https://images.unsplash.com/photo-1503376760367-13eea3481f44?q=80&w=1600&auto=format&fit=crop";
            description = "Khẳng định vị thế, chinh phục mọi địa hình với không gian rộng rãi và công nghệ off-road hàng đầu.";
            price1 = "5.150.000.000 đ";
            price2 = "7.890.000.000 đ";
        } else if ("sport".equals(type)) {
            title = "Thể Thao Hiệu Suất";
            image = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=1600&auto=format&fit=crop";
            description = "Động cơ mạnh mẽ, cảm giác lái phấn khích trên từng dặm đường với thiết kế khí động học hoàn hảo.";
            price1 = "9.450.000.000 đ";
            price2 = "12.800.000.000 đ";
        } else if ("ev".equals(type)) {
            title = "Xe Thuần Điện";
            image = "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?q=80&w=1600&auto=format&fit=crop";
            description = "Công nghệ tương lai, thân thiện với môi trường, vận hành tĩnh lặng nhưng bứt tốc đầy mạnh mẽ.";
            price1 = "3.850.000.000 đ";
            price2 = "5.190.000.000 đ";
        } else if ("coupe".equals(type)) {
            title = "Coupe Cá Tính";
            image = "https://images.unsplash.com/photo-1617531653332-bd46c24f2068?q=80&w=1600&auto=format&fit=crop";
            description = "Đường nét mượt mà, phong cách thiết kế độc bản đầy cuốn hút dành cho những ai tìm kiếm sự khác biệt.";
            price1 = "6.120.000.000 đ";
            price2 = "8.450.000.000 đ";
        } else if ("cabriolet".equals(type)) {
            title = "Cabriolet Thượng Lưu";
            image = "https://images.unsplash.com/photo-1583121274602-3e2820c69888?q=80&w=1600&auto=format&fit=crop";
            description = "Tận hưởng sự tự do trọn vẹn dưới bầu trời rộng mở cùng ngôn ngữ thiết kế gợi cảm và thu hút.";
            price1 = "7.550.000.000 đ";
            price2 = "10.200.000.000 đ";
        }

        return Templates.carDetail(session, title, image, description, price1, price2);
    }

    @GET
    @Path("cars")
    public TemplateInstance cars(@CookieParam(SESSION_COOKIE) String sessionId) {
        SessionData session = sessionManager.getSession(sessionId);
        List<Category> categories = categoryService.getAllCategoriesEntities();
        List<Brand> brands = brandService.getAllBrandEntities();
        List<Listing> listings = listingService.getActiveListingEntities();
        return Templates.cars(session, categories, brands, listings);
    }
}
