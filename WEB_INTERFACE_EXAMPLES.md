# Extending the Web Interface - Template Examples

This guide shows how to extend the web interface with additional pages following the same pattern.

## Example 1: Cars Management Page

### Step 1: Create cars.html template

Save as: `src/main/resources/templates/cars.html`

```html
{#include base.html title="Xe Hơi" showPageHeader=true}

<div class="row mb-4">
  <div class="col-md-6">
    <h2>Danh Sách Xe Hơi</h2>
  </div>
  <div class="col-md-6 text-end">
    <a href="/cars/new" class="btn btn-primary">
      <i class="bi bi-plus-circle"></i> Thêm Xe Mới
    </a>
  </div>
</div>

{#if cars.isEmpty}
<div class="alert alert-info text-center" role="alert">
  <p>Chưa có xe nào. <a href="/cars/new">Thêm xe mới</a></p>
</div>
{#else}
<div class="table-responsive">
  <table class="table table-hover">
    <thead class="table-light">
      <tr>
        <th>Tên Xe</th>
        <th>Thương Hiệu</th>
        <th>Danh Mục</th>
        <th>Giá</th>
        <th>Năm SX</th>
        <th>Hành Động</th>
      </tr>
    </thead>
    <tbody>
      {#for car in cars}
      <tr>
        <td><strong>{car.name}</strong></td>
        <td>{car.brand.name}</td>
        <td>{car.category.name}</td>
        <td>{car.price:nf('0.00')} đ</td>
        <td>{car.yearOfManufacture}</td>
        <td>
          <a href="/cars/{car.id}/edit" class="btn btn-sm btn-outline-primary">
            <i class="bi bi-pencil"></i>
          </a>
          <a
            href="/cars/{car.id}/delete"
            class="btn btn-sm btn-outline-danger"
            onclick="return confirm('Xóa?')"
          >
            <i class="bi bi-trash"></i>
          </a>
        </td>
      </tr>
      {/for}
    </tbody>
  </table>
</div>
{/if} {/include}
```

### Step 2: Create car-form.html template

```html
{#include base.html title="{isEdit ? 'Sửa xe' : 'Thêm xe mới'}" showPageHeader=true}

<div class="row">
    <div class="col-md-8 mx-auto">
        <div class="card">
            <div class="card-body p-4">
                <form method="post"
                      action="{#if isEdit}/cars/{car.id}/update{#else}/cars/create{/if}">

                    <div class="mb-3">
                        <label for="name" class="form-label">Tên Xe <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="name" name="name"
                               value="{#if car}{car.name}{/if}" required>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="brandId" class="form-label">Thương Hiệu <span class="text-danger">*</span></label>
                            <select class="form-select" id="brandId" name="brandId" required>
                                <option value="">-- Chọn thương hiệu --</option>
                                {#for brand in brands}
                                    <option value="{brand.id}"
                                            {#if car && car.brand.id == brand.id}selected{/if}>
                                        {brand.name}
                                    </option>
                                {/for}
                            </select>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="categoryId" class="form-label">Danh Mục <span class="text-danger">*</span></label>
                            <select class="form-select" id="categoryId" name="categoryId" required>
                                <option value="">-- Chọn danh mục --</option>
                                {#for category in categories}
                                    <option value="{category.id}"
                                            {#if car && car.category.id == category.id}selected{/if}>
                                        {category.name}
                                    </option>
                                {/for}
                            </select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="price" class="form-label">Giá (VND) <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" id="price" name="price"
                                   value="{#if car}{car.price}{/if}" step="1000000" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="year" class="form-label">Năm Sản Xuất</label>
                            <input type="number" class="form-control" id="year" name="year"
                                   value="{#if car}{car.yearOfManufacture}{/if}" min="1990" max="2099">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Mô Tả</label>
                        <textarea class="form-control" id="description" name="description" rows="4">
{#if car}{car.description}{/if}</textarea>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-check-circle"></i> {#if isEdit}Cập Nhật{#else}Tạo Mới{/if}
                        </button>
                        <a href="/cars" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left"></i> Quay Lại
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

{/include}
```

### Step 3: Update WebUIController.java

Add to the `Templates` class:

```java
public static native TemplateInstance cars(java.util.List<Car> cars);
public static native TemplateInstance carForm(boolean isEdit, Car car,
    java.util.List<Brand> brands, java.util.List<Category> categories);
```

Add endpoints:

```java
@GET
@Path("cars")
public TemplateInstance listCars() {
    return Templates.cars(carService.getAllCars());
}

@GET
@Path("cars/new")
public TemplateInstance newCarForm() {
    return Templates.carForm(false, null,
        brandService.getAllBrands(),
        categoryService.getAllCategoriesEntities());
}

@GET
@Path("cars/{id}/edit")
public TemplateInstance editCarForm(@PathParam("id") Long id) {
    Car car = carService.getCarById(id);
    if (car == null) {
        throw new NotFoundException("Car not found");
    }
    return Templates.carForm(true, car,
        brandService.getAllBrands(),
        categoryService.getAllCategoriesEntities());
}

@POST
@Path("cars/create")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public Response createCar(
        @FormParam("name") String name,
        @FormParam("brandId") Long brandId,
        @FormParam("categoryId") Long categoryId,
        @FormParam("price") Double price,
        @FormParam("year") Integer year,
        @FormParam("description") String description) {

    Car car = carService.createCar(name, brandId, categoryId, price, year, description);
    return Response.seeOther(java.net.URI.create("/cars")).build();
}

@POST
@Path("cars/{id}/update")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public Response updateCar(@PathParam("id") Long id, ...) {
    // Similar to create
    Car car = carService.getCarById(id);
    // Update fields
    carService.updateCar(car);
    return Response.seeOther(java.net.URI.create("/cars")).build();
}

@GET
@Path("cars/{id}/delete")
public Response deleteCar(@PathParam("id") Long id) {
    carService.deleteCar(id);
    return Response.seeOther(java.net.URI.create("/cars")).build();
}
```

---

## Example 2: Brands Management

Create `brands.html` with similar structure:

```html
{#include base.html title="Thương Hiệu" showPageHeader=true}

<div class="row mb-4">
  <div class="col-md-6">
    <h2>Danh Sách Thương Hiệu</h2>
  </div>
  <div class="col-md-6 text-end">
    <a href="/brands/new" class="btn btn-primary">
      <i class="bi bi-plus-circle"></i> Thêm Thương Hiệu
    </a>
  </div>
</div>

{#if brands.isEmpty}
<div class="alert alert-info">
  <p>Chưa có thương hiệu. <a href="/brands/new">Tạo mới</a></p>
</div>
{#else}
<div class="row">
  {#for brand in brands}
  <div class="col-md-6 mb-3">
    <div class="card">
      <div class="card-body">
        <h5 class="card-title">{brand.name}</h5>
        <p class="card-text">{brand.description}</p>
        <p class="text-muted">
          <small>{brand.foundedYear} · {brand.country}</small>
        </p>
        <div>
          <a
            href="/brands/{brand.id}/edit"
            class="btn btn-sm btn-outline-primary"
            >Sửa</a
          >
          <a
            href="/brands/{brand.id}/delete"
            class="btn btn-sm btn-outline-danger"
            >Xóa</a
          >
        </div>
      </div>
    </div>
  </div>
  {/for}
</div>
{/if} {/include}
```

---

## Example 3: Orders Management

Create `orders.html` with table layout:

```html
{#include base.html title="Đơn Hàng" showPageHeader=true}

<div class="table-responsive">
  <table class="table table-striped table-hover">
    <thead class="table-dark">
      <tr>
        <th>Mã Đơn</th>
        <th>Khách Hàng</th>
        <th>Tổng Tiền</th>
        <th>Trạng Thái</th>
        <th>Ngày Đặt</th>
        <th>Hành Động</th>
      </tr>
    </thead>
    <tbody>
      {#for order in orders}
      <tr>
        <td>{order.id}</td>
        <td>{order.user.name}</td>
        <td>{order.totalAmount:nf('0.00')} đ</td>
        <td>
          <span class="badge bg-{getStatusColor(order.status)}">
            {order.status}
          </span>
        </td>
        <td>{order.createdDate}</td>
        <td>
          <a href="/orders/{order.id}" class="btn btn-sm btn-info">
            <i class="bi bi-eye"></i>
          </a>
        </td>
      </tr>
      {/for}
    </tbody>
  </table>
</div>

{/include}
```

---

## Qute Template Syntax Cheat Sheet

```html
<!-- Variables -->
{variable} {object.property} {list.0}

<!-- Conditionals -->
{#if condition} Content {#else} Other content {/if}

<!-- Loops -->
{#for item in list} {item} {/for}

<!-- Include base layout -->
{#include base.html title="Page Title" showPageHeader=true}

<!-- Formatting -->
{variable:nf('0.00')}
<!-- Number format -->
{variable:h}
<!-- HTML escape -->

<!-- Link generation -->
href="/path/{id}" href="/path?param={value}"
```

---

## Best Practices

1. **Naming Convention**: Use kebab-case for template files (`user-form.html`)
2. **Layout Inheritance**: Always use `{#include base.html}` for consistency
3. **Form IDs**: Make IDs unique and descriptive (match field names)
4. **Validation**: Add `required` attribute to mandatory fields
5. **Feedback**: Show alerts for success/error messages
6. **Mobile**: Test on mobile devices using Bootstrap's responsive features

---

## Testing New Pages

1. Add method to `WebUIController.java`
2. Inject required services
3. Run: `mvnw quarkus:dev`
4. Access: `http://localhost:8080/your-path`
5. Check browser console for errors

---

For more template examples and Qute documentation:
https://quarkus.io/guides/qute
