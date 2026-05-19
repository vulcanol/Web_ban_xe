# Web Interface Setup Guide (Bootstrap + Thymeleaf/Qute)

## Overview

This project now includes a modern web interface built with:

- **Bootstrap 5.3** - Responsive CSS framework
- **Qute** - Quarkus's native template engine (similar to Thymeleaf)
- **Bootstrap Icons** - Icon library for UI elements

## Project Structure

```
src/main/
├── java/org/acme/resource/
│   └── WebUIController.java       # Web UI controller serving HTML pages
├── resources/
│   ├── templates/                 # Qute template files
│   │   ├── base.html              # Base layout template
│   │   ├── index.html             # Home page
│   │   ├── categories.html        # Categories list
│   │   └── category-form.html     # Create/Edit category form
│   └── static/
│       ├── css/
│       │   └── custom.css         # Custom Bootstrap overrides
│       └── js/
│           └── main.js            # Client-side JavaScript
```

## Features

### 1. Responsive Design

- Mobile-first approach using Bootstrap Grid System
- Responsive navigation menu with hamburger on mobile devices
- Flexible layouts that adapt to different screen sizes

### 2. Category Management

- **List Categories**: View all categories with thumbnail images
- **Create Category**: Add new categories with name, description, and icon URL
- **Edit Category**: Modify existing category information
- **Delete Category**: Remove categories with confirmation dialog

### 3. Components

- **Navigation Bar**: Quick access to all main sections
- **Cards**: Product display with hover animations
- **Forms**: User-friendly forms with validation feedback
- **Alerts**: Success/error messages with auto-dismiss
- **Footer**: Consistent footer across all pages

## Running the Application

### Build the project:

```bash
./mvnw clean package
```

### Run in development mode:

```bash
./mvnw quarkus:dev
```

### Access the web interface:

- Home: `http://localhost:8080/`
- Categories: `http://localhost:8080/categories`
- Create Category: `http://localhost:8080/categories/new`

## Technologies Used

### Frontend

- **Bootstrap 5.3.0** - CSS Framework
- **Bootstrap Icons** - Icon set
- **Qute** - Template engine (Quarkus native)

### Backend

- **Quarkus** - Java framework
- **Hibernate ORM** - Database ORM
- **MySQL** - Database

## Template Files Explanation

### base.html

Main layout template that all pages inherit from:

- Navigation bar with links
- Page title and subtitle
- Message/Alert display
- Content insertion point
- Footer

### categories.html

Displays a list of all categories:

- Grid layout with Bootstrap cards
- Image preview from icon URL
- Edit and Delete buttons
- Empty state message
- Create new category button

### category-form.html

Form for creating and editing categories:

- Name input (required)
- Description textarea
- Icon URL input with preview
- Back button to return to list
- Responsive layout

### index.html

Home page with:

- Welcome section
- Quick stats cards
- Links to main sections
- Quick reference guide

## Customization

### Colors

Modify CSS variables in `base.html` and `custom.css`:

```css
:root {
  --primary-color: #0d6efd;
  --secondary-color: #6c757d;
}
```

### Bootstrap Theme

Edit `custom.css` to override Bootstrap default styles:

- Button styles
- Card effects
- Form styling
- Animation effects

### Adding New Pages

1. Create template in `src/main/resources/templates/`
2. Add method in `WebUIController.java`
3. Use `{#include base.html}` to inherit base layout

## Form Handling

### POST Endpoints

- `POST /categories/create` - Create new category
- `POST /categories/{id}/update` - Update category
- `GET /categories/{id}/delete` - Delete category

### Form Validation

- HTML5 validation on frontend
- Server-side validation in service layer

## JavaScript Features

### Auto-dismiss Alerts

```javascript
showSuccessMessage("Category created successfully!");
```

### Confirm Delete

```javascript
confirmDelete("Are you sure you want to delete this item?");
```

### Form Validation

Auto-validates Bootstrap forms on submit

## Next Steps

1. **Add More Pages**: Create pages for Cars, Brands, Orders
2. **User Authentication**: Implement login/registration
3. **Search & Filter**: Add search functionality to category list
4. **Image Upload**: Handle direct image uploads instead of URLs
5. **API Integration**: Connect REST endpoints with web pages
6. **Database Seeding**: Add initial data for development

## Dependencies Added

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-qute</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-qute</artifactId>
</dependency>
```

## Notes

- Qute is Quarkus's native templating engine (provides similar functionality to Thymeleaf)
- All templates use Qute syntax: `{variable}`, `{#if}`, `{#for}`, `{#include}`
- Bootstrap icons are loaded from CDN
- Custom CSS provides additional animations and hover effects
- JavaScript supports Bootstrap tooltips and form validation

## Troubleshooting

**Templates not found?**

- Ensure templates are in `src/main/resources/templates/`
- Rebuild the project: `./mvnw clean compile`

**CSS not loading?**

- Check static files are in `src/main/resources/static/`
- Verify file paths in template links

**Database errors?**

- Ensure MySQL is running
- Check connection properties in `application.properties`

---

For more information about Qute, visit: https://quarkus.io/guides/qute
For Bootstrap documentation: https://getbootstrap.com/docs/5.3/
