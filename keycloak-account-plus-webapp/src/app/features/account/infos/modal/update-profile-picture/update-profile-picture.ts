import {Component, ElementRef, inject, ViewChild} from '@angular/core';
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import {ImageCroppedEvent, ImageCropperComponent, LoadedImage} from 'ngx-image-cropper';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {MatButton} from '@angular/material/button';
import {MatFormField, MatHint, MatInput, MatLabel, MatSuffix} from '@angular/material/input';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-update-profile-picture',
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    ImageCropperComponent,
    MatButton,
    MatFormField,
    MatLabel,
    MatInput,
    MatIcon,
    MatHint,
    MatSuffix
  ],
  templateUrl: './update-profile-picture.html',
  styleUrl: './update-profile-picture.css',
})
export class UpdateProfilePicture {
  @ViewChild('fileInput')
  protected fileInput!: ElementRef<HTMLInputElement>;

  private readonly dialogRef = inject(MatDialogRef<UpdateProfilePicture>);
  private readonly sanitizer: DomSanitizer = inject(DomSanitizer);

  protected fileName: string | null = null;
  protected imageChangedEvent: Event | null = null;
  protected croppedImage: SafeUrl = '';

  openFileDialog(): void {
    this.fileInput.nativeElement.click();
  }

  clearFile(): void {
    this.fileName = null;
    this.fileInput.nativeElement.value = '';
  }


  protected cancel(): void {
    this.dialogRef.close();
  }

  protected submit(): void {
  }

  protected fileChangeEvent(event: Event): void {
    this.imageChangedEvent = event;
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      this.fileName = null;
      return;
    }

    // Single file
    this.fileName = input.files[0].name;
  }

  protected imageCropped(event: ImageCroppedEvent): void {
    if (event.objectUrl != null) {
      this.croppedImage = this.sanitizer.bypassSecurityTrustUrl(event.objectUrl);
    }
    // event.blob can be used to upload the cropped image
  }

  protected imageLoaded(image: LoadedImage): void {
    // show cropper
  }

  protected cropperReady(): void {
    // cropper ready
  }

  protected loadImageFailed(): void {
    // show message
  }
}
