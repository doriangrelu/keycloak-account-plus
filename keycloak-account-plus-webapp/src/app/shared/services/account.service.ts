import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UserProfile {
  id: string;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  emailVerified: boolean;
  phone?: string;
  birthday?: string;
  createdAt: string;
}

export interface UpdateProfileRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  birthday?: string;
}

export interface Session {
  id: string;
  ipAddress: string;
  startedAt: string;
  lastAccessAt: string;
  clients: string[];
  userAgent?: string;
  current: boolean;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}

export interface Credential {
  id: string;
  type: string;
  userLabel?: string;
  createdAt?: string;
}

export interface Application {
  clientId: string;
  clientName: string;
  description?: string;
  grantedScopes?: string[];
  consentDate?: string;
}

@Injectable({ providedIn: 'root' })
export class AccountService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/api/account`;

  // Profile
  getProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/profile`);
  }

  updateProfile(request: UpdateProfileRequest): Observable<UserProfile> {
    return this.http.put<UserProfile>(`${this.apiUrl}/profile`, request);
  }

  // Password
  changePassword(request: ChangePasswordRequest): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/password`, request);
  }

  // Sessions
  getSessions(): Observable<Session[]> {
    return this.http.get<Session[]>(`${this.apiUrl}/sessions`);
  }

  logoutSession(sessionId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/sessions/${sessionId}`);
  }

  logoutAllSessions(includeCurrent: boolean = false): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/sessions`, {
      params: { includeCurrent: includeCurrent.toString() }
    });
  }

  // Credentials
  getCredentials(): Observable<Credential[]> {
    return this.http.get<Credential[]>(`${this.apiUrl}/credentials`);
  }

  getTotpCredentials(): Observable<Credential[]> {
    return this.http.get<Credential[]>(`${this.apiUrl}/credentials/totp`);
  }

  removeTotp(credentialId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/credentials/totp/${credentialId}`);
  }

  // Applications
  getApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.apiUrl}/applications`);
  }

  revokeConsent(clientId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/applications/${clientId}`);
  }
}
