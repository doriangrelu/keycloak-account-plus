# Keycloak Account Plus

A modern, elegant, and fully customizable alternative to the default **Keycloak Account Console**.

**Keycloak Account Plus** provides a smoother user experience, a clean UI built on modern technologies, and a developer-friendly architecture designed for easy integration with any Keycloak-based identity ecosystem.

> **Status:** 🚧 *Work in progress – active development*  
> **Official Maintainer:** **Dorian Grelu**

---

## ✨ Overview

Keycloak’s default user account console is functional but difficult to customize or rebrand.  
**Keycloak Account Plus** offers a fresh, flexible, and theme-friendly solution with:

- A fully modern UI stack  
- Better branding and styling options  
- A more intuitive user experience  
- Clear separation between UI and backend  
- Enterprise-ready extensibility  

---

## 🖥️ Frontend

The frontend is built with:

- **Angular 21**
- **Angular Material**
- **Tailwind CSS**
- Strong modular architecture for easy theming and extensibility
- Clean, responsive layout designed to be embedded in any Keycloak setup

The goal is to make the UI *far easier to customize* than Keycloak’s legacy Freemarker/Quarkus-based templates.

---

## 🛠️ Backend

A backend service (currently in development) will wrap Keycloak’s Admin API and Authentication API to provide:

- Simplified REST endpoints for account management  
- Additional validation/business logic when needed  
- A stable integration layer for the Angular frontend  
- Optional extension points for custom identity workflows  

Planned technology stack:

- **Java 25**
- **Spring Boot 4**
- **Spring Security**
- **Keycloak Admin Client or custom REST integration**

---

## 🎯 Goals

- Replace the default Keycloak account console with a modern, maintainable UI  
- Provide a fully themeable and brandable interface  
- Offer a backend wrapper to simplify integration in enterprise environments  
- Support custom modules in the future (2FA management, devices, sessions, notifications…)  
- Remain easy to install and deploy  

---

## 📦 Installation (coming soon)

Instructions for deployment with:

- Docker / Podman  
- Kubernetes (K3s / K8s)  
- Standalone Spring Boot JAR  
- Integration with existing Keycloak themes  

---

## 🗺️ Roadmap

- [ ] Implement core profile pages (email, name, phone…)  
- [ ] Add password & credential management  
- [ ] Add 2FA (TOTP / WebAuthn) management  
- [ ] Build Spring Boot backend wrapper  
- [ ] Add support for custom modules  
- [ ] Add theming system + presets  
- [ ] Provide Docker images  
- [ ] Provide Keycloak theme packaging guide  

---

## 🤝 Contributing

Contributions are welcome once the first stable structure is in place.  
Discussions, ideas, and feedback are encouraged.

---

## 📄 License

This project is licensed under the **MIT License (Modified – Preservation of Moral Rights)**.  
See the `LICENSE` file for details.

---

## 👤 Maintainer

**Dorian Grelu**  
Founder & official maintainer of Keycloak Account Plus

---

## ⭐ Support the Project

If you find this project useful, consider starring the repository — it helps visibility and motivates continued development!

---

