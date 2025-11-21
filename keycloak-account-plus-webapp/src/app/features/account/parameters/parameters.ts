import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatFormField, MatHint, MatInput, MatLabel} from '@angular/material/input';
import {MatIcon} from '@angular/material/icon';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-parameters',
  imports: [
    FormsModule,
    MatButton,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatFormField,
    MatHint,
    MatIcon,
    MatInput,
    MatLabel,
    NgOptimizedImage,
    ReactiveFormsModule
  ],
  templateUrl: './parameters.html',
  styleUrl: './parameters.css',
})
export class Parameters {

  private readonly formBuilder: FormBuilder = inject(FormBuilder);

  protected readonly form: FormGroup = this.formBuilder.group({
    password: ['', Validators.required],
    confirmPassword: ['', Validators.required],
  });

}
