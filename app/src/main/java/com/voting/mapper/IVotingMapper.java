package com.voting.mapper;

import com.voting.entity.BlacklistAPIEntity;
import com.voting.entity.CandidateAPIEntity;

/**
 * Mapper definitions which can be accessed by the service
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public interface IVotingMapper {
    /**
     * Performs network operations to fetch all candidates
     * @return valid candidate result data of type {@link CandidateAPIEntity}
     * @throws Exception when I/o error occurs
     */
    CandidateAPIEntity fetchAllCandidates() throws Exception;

    /**
     * Performs network operations to fetch all blacklisted  users
     * @return valid blacklisted result data of type {@link BlacklistAPIEntity}
     * @throws Exception when I/O error occurs
     */
    BlacklistAPIEntity fetchAllBlacklisted() throws Exception;
}
