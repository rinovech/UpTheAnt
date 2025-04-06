package com.uptheant.demo.scheduler;

import com.uptheant.demo.model.Auction;
import com.uptheant.demo.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {
    private static final Logger logger = LoggerFactory.getLogger(AuctionScheduler.class);
    private final AuctionRepository auctionRepository;


    @Scheduled(fixedRate = 1 * 60 * 1000) 
    @Transactional
    public void startScheduledAuctions() {
        LocalDateTime now = LocalDateTime.now();
        
        List<Auction> auctionsToStart = auctionRepository.findByStatusFalseAndStartTimeBefore(now);
        
        for (Auction auction : auctionsToStart) {
            try {
                auction.setStatus(true);
                auctionRepository.save(auction);
                
                logger.info("Auction started: ID {}, Name: {}", auction.getAuctionId(), auction.getName());
            } catch (Exception e) {
                logger.error("Error starting auction ID {}: {}", auction.getAuctionId(), e.getMessage());
            }
        }
    }

    @Scheduled(fixedRate = 1 * 60 * 1000) 
    @Transactional
    public void closeExpiredAuctions() {
        LocalDateTime now = LocalDateTime.now();

        List<Auction> expiredAuctions = auctionRepository.findByStatusTrueAndEndTimeBefore(now);
        
        for (Auction auction : expiredAuctions) {
            try {
                auction.setStatus(false);

                if (auction.getCurrentBid() != null) {
                    logger.info("Auction closed with winner. Auction ID: {}, Winning Bid: {}", 
                            auction.getAuctionId(), auction.getCurrentBid());
                } else {
                    logger.info("Auction closed without bids. Auction ID: {}", auction.getAuctionId());
                }

                auctionRepository.save(auction);
                
                logger.info("Auction closed: ID {}, Name: {}", auction.getAuctionId(), auction.getName());
            } catch (Exception e) {
                logger.error("Error closing auction ID {}: {}", auction.getAuctionId(), e.getMessage());
            }
        }
    }
}
