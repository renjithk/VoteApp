package com.voting.service;

import com.voting.entity.CandidateEntity;
import com.voting.entity.ChartDataEntity;
import com.voting.entity.UserEntity;

import java.util.List;

/**
 * Provides available service operations
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public interface IVotingService {
    /**
     * Synchronizes all data with server
     */
    void syncAllData();

    /**
     * Checks whether the data is empty on the local storage or outdated if it is already present
     * @return true if the data is empty or outdated and false otherwise
     */
    boolean isDataEmptyOrOutdated();

    /**
     * Returns a list of all available candidates
     * @return valid list of candidates of type {@link CandidateEntity}
     */
    List<CandidateEntity> getAllCandidates();

    /**
     * Registers vote for user
     * @param user logged in user of type {@link UserEntity}
     * @param candidate chosen candidate of type {@link CandidateEntity} which user registered their vote to
     * @return valid status code defined in {@link com.voting.utils.StatusOptions}
     */
    int registerVote(UserEntity user, CandidateEntity candidate);

    /**
     * Checks status of th user before letting them proceed in to voting
     * <p>
     *     If this is a new user, add them to users table. No need to check for blacklisting now,
     *     that will be checked during voting.
     * </p>
     * <p>
     *     If this is an existing user, check the following
     *     1. Check if the user is blacklisted - don't proceed to vote and alert user
     *     2. Check if the user has already voted - alert them and show the stats
     * </p>
     * @param user a valid user instance of type {@link UserEntity}
     * @return a valid status code mentioned in {@link com.voting.utils.StatusOptions}
     */
    int proceedWithSignIn(UserEntity user);

    /**
     * Finds votes won by each party
     * @return valid entity of type {@link ChartDataEntity}
     */
    ChartDataEntity votesPerParty();

    /**
     * Finds votes won by each candidate
     * @return valid entity of type {@link ChartDataEntity}
     */
    ChartDataEntity votesPerCandidate();
}
