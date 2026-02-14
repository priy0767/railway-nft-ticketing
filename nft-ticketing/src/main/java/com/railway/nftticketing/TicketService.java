package com.railway.nftticketing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

import jakarta.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class TicketService { // <--- THIS WAS MISSING BEFORE!

    private Web3j web3j;
    private Credentials credentials;

    @Value("${blockchain.rpc-url}")
    private String rpcUrl;

    @Value("${blockchain.private-key}")
    private String privateKey;

    @Value("${blockchain.contract-address}")
    private String contractAddress;

    // 🚀 CONSTANTS
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(30_000_000_000L); // 30 Gwei
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(500_000);
    private static final long CHAIN_ID = 80002L; // Chain ID for Polygon Amoy

    @PostConstruct
    public void init() {
        try {
            System.out.println("🌍 Connecting to Polygon Amoy...");
            this.web3j = Web3j.build(new HttpService(rpcUrl));
            this.credentials = Credentials.create(privateKey);

            System.out.println("✅ Wallet Loaded: " + credentials.getAddress());
            System.out.println("✅ Contract Address: " + contractAddress);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1. BUY TICKET (Fixed Data Order)
    public String bookTicket(String passengerName, String trainId, String date, String seatNumber) throws Exception {
        System.out.println("⏳ Minting ticket for: " + passengerName);

        // 🚨 ORDER FIXED: Train ID, Date, Seat, Name
        Function function = new Function(
                "bookTicket",
                Arrays.asList(
                        new Address(credentials.getAddress()),
                        new Utf8String(trainId),       // 1. Train ID
                        new Utf8String(date),          // 2. Date
                        new Utf8String(seatNumber),    // 3. Seat
                        new Utf8String(passengerName)  // 4. Name
                ),
                Collections.emptyList()
        );

        String encodedFunction = FunctionEncoder.encode(function);
        TransactionManager txManager = new RawTransactionManager(web3j, credentials, CHAIN_ID);

        EthSendTransaction response = txManager.sendTransaction(
                GAS_PRICE,
                GAS_LIMIT,
                contractAddress,
                encodedFunction,
                BigInteger.ZERO
        );

        if (response.hasError()) {
            return "Error: " + response.getError().getMessage();
        }

        String txHash = response.getTransactionHash();
        System.out.println("🎉 Ticket Minted! Hash: " + txHash);
        return txHash;
    }

    // 2. VERIFY TICKET (Safe Decoder)
    public String verifyTicket(BigInteger tokenId) {
        try {
            Function function = new Function(
                    "verifyTicket",
                    Arrays.asList(new Uint256(tokenId)),
                    Arrays.asList(
                            new TypeReference<Address>() {},    // 1. Owner
                            new TypeReference<Utf8String>() {}, // 2. Train ID
                            new TypeReference<Utf8String>() {}  // 3. Seat Number
                    )
            );

            String encodedFunction = FunctionEncoder.encode(function);

            EthCall response = web3j.ethCall(
                    Transaction.createEthCallTransaction(credentials.getAddress(), contractAddress, encodedFunction),
                    DefaultBlockParameterName.LATEST
            ).send();

            if (response.hasError()) {
                return "❌ Blockchain Error: " + response.getError().getMessage();
            }

            String responseValue = response.getValue();
            if (responseValue == null || responseValue.equals("0x")) {
                return "⚠️ Ticket ID " + tokenId + " not found.";
            }

            List<Type> decoded = FunctionReturnDecoder.decode(responseValue, function.getOutputParameters());

            if (decoded.isEmpty()) return "⚠️ Ticket not found.";

            String owner = decoded.get(0).getValue().toString();
            String trainId = decoded.get(1).getValue().toString();
            String seat = decoded.get(2).getValue().toString();

            return "✅ VALID: Seat " + seat + " (Train " + trainId + ")";

        } catch (Exception e) {
            return "❌ Error: Ticket ID " + tokenId + " invalid.";
        }
    }
}