# Generated by Django 3.1.6 on 2021-02-16 01:50

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('clients', '0002_auto_20210212_1925'),
    ]

    operations = [
        migrations.AlterField(
            model_name='client',
            name='photo',
            field=models.ImageField(default='images/default.png', upload_to='images/'),
        ),
    ]