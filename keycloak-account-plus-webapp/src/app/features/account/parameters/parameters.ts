import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatFormField, MatInput, MatLabel, MatError } from '@angular/material/input';
import { MatIcon } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatList, MatListItem } from '@angular/material/list';
import { MatDivider } from '@angular/material/divider';
import { DatePipe } from '@angular/common';
import { AccountService, Credential } from '@app/shared/services/account.service';

@Component({
  selector: 'app-parameters',
  imports: [
    MatButton,
    MatFormField,
    MatIcon,
    MatInput,
    MatLabel,
    MatError,
    ReactiveFormsModule,
    MatProgressSpinner,
    MatList,
    MatListItem,
    MatDivider,
    DatePipe
  ],
  templateUrl: './parameters.html',
  styleUrl: './parameters.css',
})
export class Parameters implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly accountService = inject(AccountService);
  private readonly snackBar = inject(MatSnackBar);

  protected readonly saving = signal(false);
  protected readonly loadingCredentials = signal(true);
  protected readonly credentials = signal<Credential[]>([]);

  protected readonly form: FormGroup = this.formBuilder.group({
    currentPassword: ['', [Validators.required]],
    newPassword: ['', [Validators.required, Validators.minLength(8)]],
    confirmPassword: ['', [Validators.required]],
  });

  ngOnInit(): void {
    this.loadCredentials();
  }

  protected loadCredentials(): void {
    this.loadingCredentials.set(true);
    this.accountService.getCredentials().subscribe({
      next: (credentials) => {
        this.credentials.set(credentials);
        this.loadingCredentials.set(false);
      },
      error: (error) => {
        console.error('Failed to load credentials', error);
        this.loadingCredentials.set(false);
      }
    });
  }

  protected changePassword(): void {
    if (this.form.invalid) {
      this.snackBar.open('Veuillez remplir tous les champs', 'Fermer', { duration: 3000 });
      return;
    }

    const formValue = this.form.value;

    if (formValue.newPassword !== formValue.confirmPassword) {
      this.snackBar.open('Les mots de passe ne correspondent pas', 'Fermer', { duration: 3000 });
      return;
    }

    this.saving.set(true);

    this.accountService.changePassword({
      currentPassword: formValue.currentPassword,
      newPassword: formValue.newPassword,
      confirmPassword: formValue.confirmPassword
    }).subscribe({
      next: () => {
        this.snackBar.open('Mot de passe modifié avec succès', 'Fermer', { duration: 3000 });
        this.form.reset();
        this.saving.set(false);
      },
      error: (error) => {
        console.error('Failed to change password', error);
        this.snackBar.open('Erreur lors du changement de mot de passe', 'Fermer', { duration: 5000 });
        this.saving.set(false);
      }
    });
  }

  protected removeTotp(credentialId: string): void {
    this.accountService.removeTotp(credentialId).subscribe({
      next: () => {
        this.snackBar.open('Authentification 2FA supprimée', 'Fermer', { duration: 3000 });
        this.loadCredentials();
      },
      error: (error) => {
        console.error('Failed to remove TOTP', error);
        this.snackBar.open('Erreur lors de la suppression du 2FA', 'Fermer', { duration: 5000 });
      }
    });
  }

  protected getTotpCredentials(): Credential[] {
    return this.credentials().filter(c => c.type === 'otp');
  }

  protected getPasswordCredential(): Credential | undefined {
    return this.credentials().find(c => c.type === 'password');
  }
}
