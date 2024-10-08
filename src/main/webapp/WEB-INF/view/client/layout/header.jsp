<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="/resources/taglib.jsp" %>
<!-- Spinner Start -->
<div id="spinner" class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50  d-flex align-items-center justify-content-center">
    <div class="spinner-grow text-primary" role="status"></div>
</div>
<!-- Spinner End -->


<!-- Navbar start -->
<div class="container-fluid fixed-top">
    <div class="container topbar bg-primary d-none d-lg-block">
        <div class="d-flex justify-content-between">
            <div class="top-info ps-2">
                <small class="me-3"><i class="fas fa-map-marker-alt me-2 text-secondary"></i> <a href="#" class="text-white">D21/3 Trần Não, An Khánh, Tp.Thủ Đức, Tp.HCM</a></small>
            </div>
            <div class="top-link pe-2">
                <a href="#" class="text-white"><small class="text-white mx-2">nguyenminhdao002@gmail.com</small></a>
            </div>
        </div>
    </div>
    <div class="container px-0">
        <nav class="navbar navbar-light bg-white navbar-expand-xl">
            <a href="/" class="navbar-brand"><h1 class="text-primary display-6">LaptopShop</h1></a>
            <button class="navbar-toggler py-2 px-3" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
                <span class="fa fa-bars text-primary"></span>
            </button>
            <div class="collapse navbar-collapse bg-white justify-content-between mx-5" id="navbarCollapse">
                <div class="navbar-nav">
                    <a href="/" class="nav-item nav-link">Trang Chủ</a>
                    <a href="/products" class="nav-item nav-link">Sản Phẩm</a>
                    <a href="/contact" class="nav-item nav-link">Liên Hệ</a>
                </div>
                <div class="d-flex m-3 me-0">
                    <c:if test="${not empty pageContext.request.userPrincipal}">
                        <a href="/cart-detail" class="position-relative me-4 my-auto">
                            <i class="fa fa-shopping-bag fa-2x"></i>
                            <span class="position-absolute bg-secondary rounded-circle d-flex align-items-center justify-content-center text-dark px-1" style="top: -5px; left: 15px; height: 20px; min-width: 20px;">
                                ${sessionScope.sum}
                            </span>
                        </a>
                        <div class="dropdown my-auto">
                            <a href="#" class="dropdown" role="button" id="dropdownMenuLink"
                                    data-bs-toggle="dropdown" aria-expanded="false" data-bs-toggle="dropdown"
                                    aria-expanded="false">
                                    <i class="fas fa-user fa-2x"></i>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end p-4" arialabelledby="dropdownMenuLink">
                                <li class="d-flex align-items-center flex-column" style="min-width: 300px;">
                                    <img style="width: 150px; height: 150px; border-radius: 50%; overflow: hidden;"
                                        src="/images/avatar/${sessionScope.avatar}" />
                                    <div class="text-center my-3">
                                        <c:out value="${sessionScope.fullName}"/> 
                                    </div>
                                </li>
                                <c:if test="${sessionScope.role == 'ADMIN'}">
                                    <li><a class="dropdown-item" href="/admin">Trang quản trị</a></li>
                                </c:if>
                                <li><a class="dropdown-item" href="#">Quản lý tài khoản</a></li>
                                <li><a class="dropdown-item" href="/order-history">Lịch sử mua hàng</a></li>
                                <li>
                                <hr class="dropdown-divider">
                                </li>
                                <li>
                                    <form action="/logout" method="post">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <button type="submit" class="dropdown-item">Đăng xuất</button>
                                    </form>
                                </li>
                            </ul>
                        </div>
                    </c:if>
                    <c:if test="${empty pageContext.request.userPrincipal}">
                        <a href="/login" class="position-relative me-4 my-auto">Đăng nhập</a>
                    </c:if>
                </div>
            </div>
        </nav>
    </div>
</div>
<!-- Navbar End -->