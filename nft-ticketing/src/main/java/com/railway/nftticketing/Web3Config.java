package com.railway.nftticketing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3Config {

    @Bean
    public Web3j web3j() {
        // Connect to the local Remix blockchain (or Hardhat)
        // If you are using Remix VM, we actually need an external provider like Infura or a local node.
        // For now, let's assume we are connecting to a local node (standard port 8545).
        return Web3j.build(new HttpService("http://localhost:8545"));
    }
}