# SimpleGit

SimpleGit je funkcionalan distribuirani sistem koji će da obezbedi verzioniranje za ASCII kodirane tekstualne datoteke. Sistem korisniku omogućava sledeće:
* Dodavanje nove datoteke sa jedinstvenim nazivom i putanjom u sistem.
* Dohvatanje proizvoljne datoteke iz distribuiranog sistema.
* Održavanje verzija za datoteke i razrešavanje konflikta.
* Topološka organizacija sistema omogućava brže dohvatanje bitnih datoteka.
* Otpornost na otkaze.

Kao implementacija distribuiranog sistema koristi se CHORD:

![Chord image](https://upload.wikimedia.org/wikipedia/commons/2/20/Chord_network.png)



Osnovne funkcionalnosti za sistem uključuju:
* Add - Dodavanje nove datoteke u sistem.
* Pull - Dohvatanje datoteke koja trenutno nije prisutna na čvoru.
* Commit - Upisivanje izmenjene datoteke u sistem.
* Remove - Uklanjanje datoteke iz sistema.

Dva direktorijuma koji su na početku prazni, i koji se koriste pri radu sa sistemom:
* Radni koren - gde će se nalaziti datoteke sa kojima korisnik želi aktivno da radi.
* Skladište - gde će se nalaziti datoteke koje su skladištene na ovom čvoru, na osnovu topologije sistema. Korisnik ne sme da radi direktno sa ovim datotekama.

Prednosti sistema:
* Otporan na otkaze
* Optimizacija za dohvatanje datoteka koje su relevantne za tim
* Razrešavanje konflikta (nudim 3 opcije)
  *  view - Dohvatanje datoteke iz sistema pod privremenim nazivom, kako bi se ispitao njen sadržaj. Nakon ove operacije se korisnik ponovo pita za opciju razrešavanja konflikta.
  *  push - Prepisivanje datoteke u sistemu sa lokalnom datotekom.
  *  pull - Prepisivanje lokalne datoteke sa datotekom iz sistem


