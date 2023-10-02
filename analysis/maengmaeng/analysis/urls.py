from django.urls import path
from . import views
app_name = 'analysis'
urlpatterns = [
    path('<str:user_id>/', views.my_analysis_view, name='my_view'),
]
