# Generated by Django 3.1.5 on 2021-02-27 00:33

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('clients', '0001_initial'),
        ('contenttypes', '0002_remove_content_type_name'),
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='OrthoticService',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('elbow_injury_location', models.CharField(choices=[('Below', 'Below'), ('Above', 'Above')], max_length=20)),
            ],
            options={
                'abstract': False,
            },
        ),
        migrations.CreateModel(
            name='PhysiotherapyService',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('conditions', models.CharField(choices=[('Amputee', 'Amputee'), ('Polio', 'Polio'), ('Spinal Cord Injury', 'Spinal Cord Injury'), ('Cerebral Palsy', 'Cerebral Palsy'), ('Spina Bifida', 'Spina Bifida'), ('Hydrocephalus', 'Hydrocephalus'), ('Visual Impairment', 'Visual Impairment'), ('Hearing Impairment', 'Hearing Impairment'), ('Other', 'Other')], max_length=100)),
                ('specified_condition', models.CharField(blank=True, default='', help_text='Other condition, please specify', max_length=100)),
            ],
            options={
                'abstract': False,
            },
        ),
        migrations.CreateModel(
            name='ProstheticService',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('knee_injury_location', models.CharField(choices=[('Below', 'Below'), ('Above', 'Above')], max_length=20)),
            ],
            options={
                'abstract': False,
            },
        ),
        migrations.CreateModel(
            name='WheelchairService',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('usage_experience', models.CharField(choices=[('Basic', 'Basic'), ('Intermediate', 'Intermediate')], max_length=100)),
                ('client_hip_width', models.FloatField(default=0)),
                ('client_has_existing_wheelchair', models.BooleanField(default=False)),
                ('is_wheel_chair_repairable', models.BooleanField()),
            ],
            options={
                'abstract': False,
            },
        ),
        migrations.CreateModel(
            name='Referral',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date_created', models.DateTimeField(auto_now_add=True)),
                ('status', models.CharField(choices=[('CREATED', 'Created'), ('RESOLVED', 'Resolved')], default='CREATED', max_length=20)),
                ('outcome', models.TextField(blank=True, default='')),
                ('service_type', models.CharField(choices=[('Wheelchair', 'Wheelchair'), ('Physiotherapy', 'Physiotherapy'), ('Prosthetic', 'Prosthetic'), ('Orthotic', 'Orthotic')], max_length=50)),
                ('service_object_id', models.PositiveIntegerField()),
                ('client', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='clients.client')),
                ('service_object_type', models.ForeignKey(limit_choices_to=models.Q(app_label='referral'), on_delete=django.db.models.deletion.CASCADE, to='contenttypes.contenttype')),
                ('user_creator', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
    ]