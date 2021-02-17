"""cbrsite URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.conf.urls.static import static
from django.contrib import admin
from django.urls import path, include
from rest_framework import routers
from rest_framework.authtoken import views as auth_view

from django.conf import settings

from authenticate.views import CustomObtainToken
from clients.views_api import ClientViewSet
from users.views import UserViewSet
from visits.views import VisitViewSet

router = routers.DefaultRouter()

# Register additional api url endpoints here
router.register(r'clients', ClientViewSet)
router.register(r'users', UserViewSet)
router.register(r'visits', VisitViewSet)

urlpatterns = \
    [
        path('admin/', admin.site.urls),
        path('api/token-auth', CustomObtainToken.as_view(), name='token-auth'),
        path('api/', include(router.urls)),
    ] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT) + static(settings.STATIC_URL,
                                                                               document_root=settings.STATIC_ROOT)
