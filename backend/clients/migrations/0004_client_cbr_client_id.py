# Generated by Django 3.1.5 on 2021-03-10 07:36

from django.db import migrations, models
import uuid

class Migration(migrations.Migration):

    dependencies = [
        ('clients', '0003_auto_20210306_2312'),
    ]

    operations = [
        migrations.AddField(
            model_name='client',
            name='cbr_client_id',
            field=models.CharField(null=True, editable=False, max_length=10, unique=True),
        ),
    ]