# Generated by Django 3.1.5 on 2021-02-28 00:17

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('referral', '0002_auto_20210227_1439'),
    ]

    operations = [
        migrations.AddField(
            model_name='referral',
            name='photo',
            field=models.ImageField(blank=True, upload_to='images/'),
        ),
    ]
