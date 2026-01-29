import { Component, inject, OnInit, signal } from '@angular/core';
import {
  MatAccordion,
  MatExpansionPanel,
  MatExpansionPanelDescription,
  MatExpansionPanelHeader,
  MatExpansionPanelTitle
} from '@angular/material/expansion';
import { DatePipe } from '@angular/common';
import { MatList, MatListItem } from '@angular/material/list';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatChip } from '@angular/material/chips';
import { AccountService, Session } from '@app/shared/services/account.service';

@Component({
  selector: 'app-access',
  imports: [
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelHeader,
    MatExpansionPanelTitle,
    MatExpansionPanelDescription,
    DatePipe,
    MatList,
    MatListItem,
    MatButton,
    MatIcon,
    MatProgressSpinner,
    MatChip
  ],
  templateUrl: './access.html',
  styleUrl: './access.css',
})
export class Access implements OnInit {
  private readonly accountService = inject(AccountService);
  private readonly snackBar = inject(MatSnackBar);

  protected readonly loading = signal(true);
  protected readonly sessions = signal<Session[]>([]);

  ngOnInit(): void {
    this.loadSessions();
  }

  protected loadSessions(): void {
    this.loading.set(true);
    this.accountService.getSessions().subscribe({
      next: (sessions) => {
        this.sessions.set(sessions);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Failed to load sessions', error);
        this.snackBar.open('Erreur lors du chargement des sessions', 'Fermer', { duration: 5000 });
        this.loading.set(false);
      }
    });
  }

  protected logoutSession(sessionId: string): void {
    this.accountService.logoutSession(sessionId).subscribe({
      next: () => {
        this.snackBar.open('Session déconnectée', 'Fermer', { duration: 3000 });
        this.loadSessions();
      },
      error: (error) => {
        console.error('Failed to logout session', error);
        this.snackBar.open('Erreur lors de la déconnexion de la session', 'Fermer', { duration: 5000 });
      }
    });
  }

  protected logoutAllSessions(): void {
    this.accountService.logoutAllSessions(false).subscribe({
      next: () => {
        this.snackBar.open('Toutes les autres sessions ont été déconnectées', 'Fermer', { duration: 3000 });
        this.loadSessions();
      },
      error: (error) => {
        console.error('Failed to logout all sessions', error);
        this.snackBar.open('Erreur lors de la déconnexion des sessions', 'Fermer', { duration: 5000 });
      }
    });
  }
}
