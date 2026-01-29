import { Component, inject, OnInit, signal } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { MatFormField, MatHint, MatInput, MatLabel } from '@angular/material/input';
import { MatIcon } from '@angular/material/icon';
import { MatDatepicker, MatDatepickerInput, MatDatepickerToggle } from '@angular/material/datepicker';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { UpdateProfilePicture } from '@features/account/infos/modal/update-profile-picture/update-profile-picture';
import { AccountService, UserProfile } from '@app/shared/services/account.service';

@Component({
  selector: 'app-infos',
  imports: [
    NgOptimizedImage,
    MatButton,
    MatFormField,
    MatLabel,
    MatInput,
    MatIcon,
    MatDatepickerInput,
    MatHint,
    MatDatepickerToggle,
    MatDatepicker,
    ReactiveFormsModule,
    MatProgressSpinner
  ],
  templateUrl: './infos.html',
  styleUrl: './infos.css',
})
export class Infos implements OnInit {
  private readonly dialog = inject(MatDialog);
  private readonly formBuilder = inject(FormBuilder);
  private readonly accountService = inject(AccountService);
  private readonly snackBar = inject(MatSnackBar);

  protected readonly loading = signal(true);
  protected readonly saving = signal(false);
  protected readonly profile = signal<UserProfile | null>(null);

  protected readonly form: FormGroup = this.formBuilder.group({
    firstName: ['', [Validators.required, Validators.maxLength(250)]],
    lastName: ['', [Validators.required, Validators.maxLength(250)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(250)]],
    phone: ['', [Validators.maxLength(50)]],
    birthday: [''],
  });

  ngOnInit(): void {
    this.loadProfile();
  }

  protected loadProfile(): void {
    this.loading.set(true);
    this.accountService.getProfile().subscribe({
      next: (profile) => {
        this.profile.set(profile);
        this.form.patchValue({
          firstName: profile.firstName || '',
          lastName: profile.lastName || '',
          email: profile.email || '',
          phone: profile.phone || '',
          birthday: profile.birthday ? new Date(profile.birthday) : null,
        });
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Failed to load profile', error);
        this.snackBar.open('Erreur lors du chargement du profil', 'Fermer', { duration: 5000 });
        this.loading.set(false);
      }
    });
  }

  protected saveProfile(): void {
    if (this.form.invalid) {
      this.snackBar.open('Veuillez corriger les erreurs du formulaire', 'Fermer', { duration: 3000 });
      return;
    }

    this.saving.set(true);
    const formValue = this.form.value;

    const request = {
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      email: formValue.email,
      phone: formValue.phone || undefined,
      birthday: formValue.birthday ? this.formatDate(formValue.birthday) : undefined,
    };

    this.accountService.updateProfile(request).subscribe({
      next: (profile) => {
        this.profile.set(profile);
        this.snackBar.open('Profil mis à jour avec succès', 'Fermer', { duration: 3000 });
        this.saving.set(false);
      },
      error: (error) => {
        console.error('Failed to save profile', error);
        this.snackBar.open('Erreur lors de la sauvegarde du profil', 'Fermer', { duration: 5000 });
        this.saving.set(false);
      }
    });
  }

  protected changeProfilPicture(): void {
    this.dialog.open(UpdateProfilePicture, {
      width: '50vw',
      height: '80vh',
    });
  }

  private formatDate(date: Date): string {
    if (!(date instanceof Date)) {
      date = new Date(date);
    }
    return date.toISOString().split('T')[0];
  }
}
