# 🚆 Railway NFT Ticketing System

A full-stack decentralized application (dApp) that issues unforgeable train tickets as unique NFTs on the Polygon blockchain. 

## 📝 Description
This project solves the problem of ticket counterfeiting by leveraging blockchain technology. Each ticket is minted as an ERC-721 token on the Polygon Amoy Testnet, ensuring that every seat reservation is verifiable, permanent, and unique. 

- **Motivation**: To explore the integration of traditional enterprise frameworks (Spring Boot) with decentralized networks.
- **Problem Solved**: Eliminates fake tickets and unauthorized seat duplications.
- **What I Learned**: Smart contract deployment with Solidity, Java-blockchain integration via Web3j, and cloud deployment of dApps on Railway.

## 🛠️ Tech Stack
- **Blockchain**: Polygon Amoy Testnet
- **Smart Contract**: Solidity (ERC-721)
- **Backend**: Java 17, Spring Boot 3.2.3
- **Blockchain Library**: Web3j
- **Frontend**: HTML5, JavaScript, Bootstrap
- **Deployment**: Railway.app

## 🏗️ Architecture Overview

1. **Frontend**: Collects passenger data and sends it to the Spring Boot REST API.
2. **Backend**: Processes requests and uses Web3j to sign transactions with a private key.
3. **Smart Contract**: Mints the NFT and stores passenger data (Seat, Train ID, Date) on-chain.
4. **Verification**: Queries the blockchain to validate ticket ownership and details.

## 🚀 Installation & Setup

### Prerequisites
- JDK 17+
- Maven
- MetaMask with Polygon Amoy Testnet funds

### Local Setup
1. **Clone the repository**:
   ```bash
   git clone [https://github.com/priy0767/railway-nft-ticketing.git](https://github.com/priy0767/railway-nft-ticketing.git)
   cd railway-nft-ticketing/nft-ticketing
