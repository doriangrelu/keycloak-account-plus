import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NgOptimizedImage } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NgOptimizedImage, MatButton, MatIcon],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private readonly keycloak = inject(KeycloakService);

  protected readonly title = signal('keycloak-account-plus-webapp');

  protected async logout(): Promise<void> {
    await this.keycloak.logout(window.location.origin);
  }
}
