# Generated by Django 3.1.5 on 2021-02-21 04:19

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('alerts', '0001_initial'),
    ]

    operations = [
        migrations.AlterModelOptions(
            name='alert',
            options={'ordering': ['-date']},
        ),
    ]
