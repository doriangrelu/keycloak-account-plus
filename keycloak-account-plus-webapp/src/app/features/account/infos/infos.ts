import {Component, inject} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatFormField, MatHint, MatInput, MatLabel} from '@angular/material/input';
import {MatIcon} from '@angular/material/icon';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';
import {UpdateProfilePicture} from '@features/account/infos/modal/update-profile-picture/update-profile-picture';


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
    ReactiveFormsModule
  ],
  templateUrl: './infos.html',
  styleUrl: './infos.css',
})
export class Infos {

  private readonly dialog: MatDialog = inject(MatDialog);
  private readonly formBuilder: FormBuilder = inject(FormBuilder);

  protected readonly form: FormGroup = this.formBuilder.group({
    firstname: ['', Validators.required, Validators.maxLength(250)],
    lastname: ['', Validators.required, Validators.maxLength(250)],
    email: ['', Validators.required, Validators.email, Validators.maxLength(250)],
    phone: ['', Validators.required, Validators.maxLength(250)],
    birthday: ['', Validators.required],
  });

  protected changeProfilPicture(): void {
    this.dialog.open(UpdateProfilePicture, {
      width: '50vw',
      height: '80vh',
    });
  }


}
