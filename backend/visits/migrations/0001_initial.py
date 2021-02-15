# Generated by Django 3.1.5 on 2021-02-13 01:45

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('clients', '__first__'),
    ]

    operations = [
        migrations.CreateModel(
            name='Visit',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('datetime_created', models.DateTimeField(auto_now_add=True)),
                ('client_state_previous', models.JSONField(blank=True, editable=False, null=True)),
                ('client_state_updated', models.JSONField(blank=True, editable=False, null=True)),
                ('client_info_changed', models.JSONField(blank=True, editable=False, null=True)),
                ('additional_notes', models.TextField(blank=True, default='', null=True, verbose_name='additional notes')),
                ('client', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='clients.client')),
                ('user_creator', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
    ]